package com.glodblock.github.appflux.common.items;

import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import ae2.api.stacks.AEKeyType;
import ae2.api.upgrades.IUpgradeInventory;
import ae2.api.upgrades.UpgradeInventories;
import ae2.api.upgrades.Upgrades;
import ae2.container.GuiIds;
import ae2.items.tools.powered.AbstractPortableCell;
import com.glodblock.github.appflux.AppFlux;
import com.glodblock.github.appflux.api.IFluxCell;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.me.cell.FECellHandler;
import com.glodblock.github.appflux.common.me.cell.FluxCellInventory;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.Validate;

import java.util.List;

public class ItemPortableFECell extends AbstractPortableCell implements IFluxCell {

    private final long totalBytes;
    private final double idleDrain;

    public ItemPortableFECell(int quartKilobytes, double idleDrain, int defaultColor) {
        super(GuiIds.GuiKey.PORTABLE_ITEM_CELL, 200000, defaultColor);
        this.totalBytes = quartKilobytes * 256L;
        this.idleDrain = idleDrain;
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        if (!world.isRemote && entity instanceof EntityPlayer player && getUpgrades(stack).isInstalled(AFItemAndBlock.INDUCTION_CARD)) {
            FluxCellInventory inv = (FluxCellInventory) FECellHandler.HANDLER.getCellInventory(stack, null);
            if (inv == null || inv.getStoredEnergy() <= 0) {
                return;
            }
            for (int slot = 0; slot < player.inventory.getSizeInventory() && inv.getStoredEnergy() > 0; slot++) {
                ItemStack target = player.inventory.getStackInSlot(slot);
                if (!target.isEmpty() && target.hasCapability(CapabilityEnergy.ENERGY, null)) {
                    IEnergyStorage energy = target.getCapability(CapabilityEnergy.ENERGY, null);
                    if (energy != null && energy.canReceive()) {
                        int added = energy.receiveEnergy(AFUtil.clampLong(inv.getStoredEnergy()), false);
                        if (added > 0) {
                            inv.extract(FluxKey.of(EnergyType.FE), added, Actionable.MODULATE, IActionSource.ofPlayer(player));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advanced) {
        super.addCheckedInformation(stack, world, lines, advanced);
        Validate.isTrue(stack.getItem() == this);
        FECellHandler.HANDLER.addCellInformationToTooltip(stack, lines);
    }

    @Override
    public ResourceLocation getRecipeId() {
        return AppFlux.id("tools/" + getRegistryName().getPath());
    }

    @Override
    public double getChargeRate(ItemStack stack) {
        return 80D + 80D * Upgrades.getEnergyCardMultiplier(getUpgrades(stack));
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
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.forItem(stack, 4);
    }
}
