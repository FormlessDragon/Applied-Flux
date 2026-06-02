package com.glodblock.github.appflux.common.items;

import ae2.api.stacks.AEKeyType;
import ae2.api.storage.StorageCells;
import ae2.api.upgrades.IUpgradeInventory;
import ae2.api.upgrades.UpgradeInventories;
import ae2.core.localization.PlayerMessages;
import ae2.items.AEBaseItem;
import ae2.util.InteractionUtil;
import com.glodblock.github.appflux.api.IFluxCell;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.caps.CellFEPower;
import com.glodblock.github.appflux.common.me.cell.FECellHandler;
import com.glodblock.github.appflux.common.me.cell.FluxCellInventory;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemFECell extends AEBaseItem implements IFluxCell {

    private final Item coreItem;
    private final long totalBytes;
    private final double idleDrain;

    public ItemFECell(Item coreItem, int kilobytes, double idleDrain) {
        this.coreItem = coreItem;
        this.totalBytes = kilobytes * 1024L;
        this.idleDrain = idleDrain;
        setMaxStackSize(1);
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.forItem(stack, 1);
    }

    @Override
    public AEKeyType getKeyType() {
        return FluxKeyType.TYPE;
    }

    @Override
    public EnergyType getEnergyType() {
        return EnergyType.FE;
    }

    @Override
    public long getBytes(ItemStack cellItem) {
        return totalBytes;
    }

    @Override
    public double getIdleDrain() {
        return idleDrain;
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advanced) {
        Validate.isTrue(stack.getItem() == this);
        FECellHandler.HANDLER.addCellInformationToTooltip(stack, lines);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        disassembleDrive(stack, world, player);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return disassembleDrive(player.getHeldItem(hand), world, player) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    private boolean disassembleDrive(ItemStack stack, World world, EntityPlayer player) {
        if (!InteractionUtil.isInAlternateUseMode(player) || world.isRemote) {
            return false;
        }
        if (player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != stack && player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND) != stack) {
            return false;
        }
        FluxCellInventory inv = (FluxCellInventory) StorageCells.getCellInventory(stack, null);
        if (inv == null || !inv.getAvailableStacks().isEmpty()) {
            player.sendStatusMessage(PlayerMessages.OnlyEmptyCellsCanBeDisassembled.text(), true);
            return false;
        }
        List<ItemStack> upgrades = new ArrayList<>();
        for (ItemStack upgrade : getUpgrades(stack)) {
            if (!upgrade.isEmpty()) {
                upgrades.add(upgrade.copy());
            }
        }
        stack.shrink(1);
        player.inventory.addItemStackToInventory(new ItemStack(coreItem));
        player.inventory.addItemStackToInventory(getHousing());
        for (ItemStack upgrade : upgrades) {
            player.inventory.addItemStackToInventory(upgrade);
        }
        return true;
    }

    public ItemStack getHousing() {
        return new ItemStack(AFItemAndBlock.FE_HOUSING);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        FluxCellInventory inv = (FluxCellInventory) FECellHandler.HANDLER.getCellInventory(stack, null);
        if (inv == null) {
            return super.initCapabilities(stack, nbt);
        }
        return new ICapabilityProvider() {
            private final CellFEPower power = new CellFEPower(inv);

            @Override
            public boolean hasCapability(@NonNull Capability<?> capability, @Nullable EnumFacing facing) {
                return capability == CapabilityEnergy.ENERGY;
            }

            @Override
            public <T> T getCapability(@NonNull Capability<T> capability, @Nullable EnumFacing facing) {
                return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(power) : null;
            }
        };
    }
}
