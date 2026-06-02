package com.glodblock.github.appflux.common.me.strategy;

import ae2.api.behaviors.ContainerItemStrategy;
import ae2.api.config.Actionable;
import ae2.api.stacks.GenericStack;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemHandlerHelper;

public class FEContainerItemStrategy implements ContainerItemStrategy<FluxKey, FEContainerItemStrategy.Context> {

    public static void register() {
        ContainerItemStrategy.register(FluxKeyType.TYPE, FluxKey.class, new FEContainerItemStrategy());
    }

    @Override
    public GenericStack getContainedStack(ItemStack stack) {
        IEnergyStorage energy = energy(stack);
        return energy != null && energy.getEnergyStored() > 0 ? new GenericStack(FluxKey.of(EnergyType.FE), energy.getEnergyStored()) : null;
    }

    @Override
    public Context findCarriedContext(EntityPlayer player, Container container) {
        ItemStack stack = player.inventory.getItemStack();
        return energy(stack) == null ? null : new Context(player, -1, stack);
    }

    @Override
    public Context findPlayerSlotContext(EntityPlayer player, int slot) {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        return energy(stack) == null ? null : new Context(player, slot, stack);
    }

    @Override
    public long extract(Context context, FluxKey what, long amount, Actionable mode) {
        IEnergyStorage energy = energy(context.stack);
        return energy == null ? 0 : energy.extractEnergy(AFUtil.clampLong(amount), mode.isSimulate());
    }

    @Override
    public long insert(Context context, FluxKey what, long amount, Actionable mode) {
        IEnergyStorage energy = energy(context.stack);
        return energy == null ? 0 : energy.receiveEnergy(AFUtil.clampLong(amount), mode.isSimulate());
    }

    @Override
    public void playFillSound(EntityPlayer player, FluxKey what) {
    }

    @Override
    public void playEmptySound(EntityPlayer player, FluxKey what) {
    }

    @Override
    public GenericStack getExtractableContent(Context context) {
        return getContainedStack(context.stack);
    }

    private static IEnergyStorage energy(ItemStack stack) {
        return !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null) ? stack.getCapability(CapabilityEnergy.ENERGY, null) : null;
    }

    public static class Context {
        private final EntityPlayer player;
        private final int slot;
        private final ItemStack stack;

        Context(EntityPlayer player, int slot, ItemStack stack) {
            this.player = player;
            this.slot = slot;
            this.stack = stack;
        }

        public void setStack(ItemStack result) {
            if (slot < 0) {
                player.inventory.setItemStack(result);
            } else {
                player.inventory.setInventorySlotContents(slot, result);
            }
        }

        public void addOverflow(ItemStack overflow) {
            ItemHandlerHelper.giveItemToPlayer(player, overflow);
        }
    }
}
