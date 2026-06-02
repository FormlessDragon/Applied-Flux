package com.glodblock.github.appflux.common.me.cell;

import ae2.api.storage.cells.ICellHandler;
import ae2.api.storage.cells.ISaveProvider;
import ae2.api.storage.cells.StorageCell;
import com.glodblock.github.appflux.common.items.ItemCreativeFECell;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class FECreativeCellHandler implements ICellHandler {

    public static final FECreativeCellHandler HANDLER = new FECreativeCellHandler();

    @Override
    public boolean isCell(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemCreativeFECell;
    }

    @Override
    public StorageCell getCellInventory(ItemStack stack, @Nullable ISaveProvider container) {
        return isCell(stack) ? FECreativeCellInventory.INSTANCE : null;
    }
}
