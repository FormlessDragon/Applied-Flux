package com.glodblock.github.appflux.common.me.service;

import ae2.api.networking.extensions.GridLogicContext;
import ae2.api.networking.extensions.GridLogicExtension;
import ae2.api.networking.storage.IStorageService;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.me.energy.EnergyHandler;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

final class GridEnergyDistributor implements IEnergyDistributor, GridLogicExtension {
    private final GridLogicContext context;
    private final EnergyHandler.SendAction[] actions = new EnergyHandler.SendAction[EnumFacing.VALUES.length];
    private EnergyDistributeService service;

    GridEnergyDistributor(GridLogicContext context) {
        this.context = context;
    }

    @Override
    public void initialize(GridLogicContext context) {
        if (this.context != context) {
            throw new IllegalArgumentException("Unexpected grid logic context");
        }
        context.getManagedNode().addService(IEnergyDistributor.class, this);
    }

    @Override
    public void distribute() {
        IStorageService storage = getStorage();
        TileEntity self = context.getHostTile();
        if (storage == null || self == null || self.getWorld() == null) {
            return;
        }

        for (EnumFacing side : context.getTargetSides()) {
            if (actions[side.ordinal()] == null) {
                TileEntity neighbor = AFUtil.neighbor(self, side);
                EnumFacing targetSide = side.getOpposite();
                actions[side.ordinal()] = neighbor != null
                        && AFUtil.getGrid(neighbor, targetSide) != context.getManagedNode().getGrid()
                        && AFUtil.isWhiteListTE(neighbor, targetSide)
                        && AFUtil.shouldTryCast(neighbor, targetSide)
                        ? EnergyHandler.getHandler(neighbor, targetSide)
                        : EnergyHandler.SendAction.NOOP;
            }
            actions[side.ordinal()].send(storage, context.getActionSource());
        }
    }

    @Nullable
    private IStorageService getStorage() {
        var grid = context.getManagedNode().getGrid();
        return grid == null ? null : grid.getStorageService();
    }

    @Override
    public boolean isActive() {
        return context.getManagedNode().isActive();
    }

    @Override
    public void setServiceHost(@Nullable EnergyDistributeService service) {
        this.service = service;
        updateSleep();
    }

    @Override
    public void onNeighborChanged(EnumFacing side) {
        actions[side.ordinal()] = null;
    }

    @Override
    public void onUpgradesChanged() {
        updateSleep();
    }

    private void updateSleep() {
        if (service == null) {
            return;
        }
        if (context.getUpgrades().isInstalled(AFItemAndBlock.INDUCTION_CARD)) {
            service.wake(this);
        } else {
            service.sleep(this);
        }
    }
}
