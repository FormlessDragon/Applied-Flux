package com.glodblock.github.appflux.common.me.cell;

import ae2.api.storage.cells.ISaveProvider;
import com.glodblock.github.appflux.api.IFluxCell;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class FECellInventory extends FluxCellInventory {

    public FECellInventory(IFluxCell cellType, ItemStack stack, @Nullable ISaveProvider container) {
        super(cellType, stack, container);
    }

    @Override
    protected EnergyType getEnergyType() {
        return EnergyType.FE;
    }
}
