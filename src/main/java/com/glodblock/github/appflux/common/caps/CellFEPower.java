package com.glodblock.github.appflux.common.caps;

import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import com.glodblock.github.appflux.common.me.cell.FluxCellInventory;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraftforge.energy.IEnergyStorage;

public class CellFEPower implements IEnergyStorage {

    private final FluxCellInventory cell;

    public CellFEPower(FluxCellInventory cell) {
        this.cell = cell;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return AFUtil.clampLong(cell.insert(FluxKey.of(EnergyType.FE), maxReceive, Actionable.ofSimulate(simulate), IActionSource.empty()));
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return AFUtil.clampLong(cell.extract(FluxKey.of(EnergyType.FE), maxExtract, Actionable.ofSimulate(simulate), IActionSource.empty()));
    }

    @Override
    public int getEnergyStored() {
        return AFUtil.clampLong(cell.getStoredEnergy());
    }

    @Override
    public int getMaxEnergyStored() {
        return AFUtil.clampLong(cell.getMaxEnergy());
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
