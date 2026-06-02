package com.glodblock.github.appflux.common.me.cell;

import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import ae2.api.stacks.AEKey;
import ae2.api.stacks.KeyCounter;
import ae2.api.storage.cells.CellState;
import ae2.api.storage.cells.ISaveProvider;
import ae2.api.storage.cells.StorageCell;
import ae2.api.upgrades.IUpgradeInventory;
import ae2.core.definitions.AEItems;
import com.glodblock.github.appflux.api.IFluxCell;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

public abstract class FluxCellInventory implements StorageCell {

    protected static final String DATA = "power";
    protected final IFluxCell cellType;
    protected final ItemStack stack;
    protected final boolean hasVoidUpgrade;
    @Nullable
    protected final ISaveProvider container;

    protected long storedEnergy;
    protected boolean persisted = true;

    protected FluxCellInventory(IFluxCell cellType, ItemStack stack, @Nullable ISaveProvider container) {
        this.cellType = cellType;
        this.stack = stack;
        this.container = container;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            storedEnergy = tag.getLong(DATA);
        }
        this.hasVoidUpgrade = getUpgrades().isInstalled(AEItems.VOID_CARD.asItem());
    }

    @Override
    public CellState getStatus() {
        if (storedEnergy <= 0) {
            return CellState.EMPTY;
        }
        if (storedEnergy >= getMaxEnergy()) {
            return CellState.FULL;
        }
        return CellState.NOT_EMPTY;
    }

    public IUpgradeInventory getUpgrades() {
        return cellType.getUpgrades(stack);
    }

    @Override
    public double getIdleDrain() {
        return cellType.getIdleDrain();
    }

    public long getStoredEnergy() {
        return storedEnergy;
    }

    public long getMaxEnergy() {
        return cellType.getBytes(stack) * FluxKeyType.TYPE.getAmountPerByte();
    }

    public long getTotalBytes() {
        return cellType.getBytes(stack);
    }

    public long getUsedBytes() {
        long amountPerByte = FluxKeyType.TYPE.getAmountPerByte();
        return (storedEnergy + amountPerByte - 1) / amountPerByte;
    }

    protected void saveChanges() {
        persisted = false;
        if (container != null) {
            container.saveChanges();
        } else {
            persist();
        }
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (!(what instanceof FluxKey) || amount <= 0) {
            return 0;
        }
        long inserted = Math.min(getMaxEnergy() - storedEnergy, amount);
        if (mode == Actionable.MODULATE && inserted > 0) {
            storedEnergy += inserted;
            saveChanges();
        }
        return hasVoidUpgrade ? amount : inserted;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (!(what instanceof FluxKey) || amount <= 0) {
            return 0;
        }
        long extracted = Math.min(storedEnergy, amount);
        if (mode == Actionable.MODULATE && extracted > 0) {
            storedEnergy -= extracted;
            saveChanges();
        }
        return extracted;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        if (storedEnergy > 0) {
            out.add(FluxKey.of(getEnergyType()), storedEnergy);
        }
    }

    @Override
    public void persist() {
        if (persisted) {
            return;
        }
        if (storedEnergy <= 0) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                tag.removeTag(DATA);
                if (tag.isEmpty()) {
                    stack.setTagCompound(null);
                }
            }
        } else {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
                stack.setTagCompound(tag);
            }
            tag.setLong(DATA, storedEnergy);
        }
        persisted = true;
    }

    protected abstract EnergyType getEnergyType();

    @Override
    public ITextComponent getDescription() {
        return new TextComponentString(stack.getDisplayName());
    }
}
