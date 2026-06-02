package com.glodblock.github.appflux.api;

import ae2.api.config.FuzzyMode;
import ae2.api.stacks.AEKeyType;
import ae2.api.storage.cells.ICellWorkbenchItem;
import ae2.api.upgrades.IUpgradeInventory;
import ae2.util.ConfigInventory;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import net.minecraft.item.ItemStack;

public interface IFluxCell extends ICellWorkbenchItem {

    AEKeyType getKeyType();

    EnergyType getEnergyType();

    long getBytes(ItemStack cellItem);

    double getIdleDrain();

    IUpgradeInventory getUpgrades(ItemStack stack);

    default int getBytesPerType(ItemStack cellItem) {
        return 8;
    }

    default int getTotalTypes(ItemStack cellItem) {
        return 1;
    }

    @Override
    default FuzzyMode getFuzzyMode(ItemStack stack) {
        return FuzzyMode.IGNORE_ALL;
    }

    @Override
    default void setFuzzyMode(ItemStack stack, FuzzyMode fuzzyMode) {
    }

    @Override
    default ConfigInventory getConfigInventory(ItemStack is) {
        return ConfigInventory.emptyTypes();
    }
}
