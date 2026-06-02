package com.glodblock.github.appflux.common.me.cell;

import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import ae2.api.stacks.AEKey;
import ae2.api.stacks.KeyCounter;
import ae2.api.storage.cells.CellState;
import ae2.api.storage.cells.StorageCell;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class FECreativeCellInventory implements StorageCell {

    public static final FECreativeCellInventory INSTANCE = new FECreativeCellInventory();

    private FECreativeCellInventory() {
    }

    @Override
    public CellState getStatus() {
        return CellState.FULL;
    }

    @Override
    public double getIdleDrain() {
        return 0;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        return what instanceof FluxKey ? amount : 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        return what instanceof FluxKey ? amount : 0;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        out.add(FluxKey.of(EnergyType.FE), Long.MAX_VALUE / 2);
    }

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("item.appflux.fe_creative_cell.name");
    }

    @Override
    public void persist() {
    }
}
