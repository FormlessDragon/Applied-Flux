package com.glodblock.github.appflux.common.tileentities;

import ae2.api.networking.GridFlags;
import ae2.api.networking.GridHelper;
import ae2.api.networking.IManagedGridNode;
import ae2.api.networking.energy.IEnergyService;
import ae2.api.networking.security.IActionSource;
import ae2.api.networking.storage.IStorageService;
import ae2.api.util.AECableType;
import ae2.tile.grid.AENetworkedTile;
import com.glodblock.github.appflux.common.caps.NetworkFEPower;
import com.glodblock.github.appflux.common.me.energy.EnergyHandler;
import com.glodblock.github.appflux.config.AFConfig;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileFluxAccessor extends AENetworkedTile implements INeighborListener {

    private final EnergyHandler.SendAction[] actions = new EnergyHandler.SendAction[6];
    private final IActionSource source = IActionSource.ofMachine(this);
    private final IEnergyStorage energyStorage = new NetworkFEPower(this::getStorage, source);

    @Override
    protected IManagedGridNode createMainNode() {
        return GridHelper.createManagedNode(this, (nodeOwner, node) -> nodeOwner.saveChanges()).setFlags(GridFlags.REQUIRE_CHANNEL).setVisualRepresentation(getItemFromTile());
    }

    @Override
    public AECableType getCableConnectionType(EnumFacing side) {
        return AECableType.SMART;
    }

    @Override
    public void update() {
        super.update();
        if (world == null || world.isRemote) {
            return;
        }
        IStorageService storage = getStorage();
        if (storage == null) {
            return;
        }
        for (EnumFacing side : EnumFacing.VALUES) {
            if (actions[side.ordinal()] == null) {
                TileEntity te = AFUtil.neighbor(this, side);
                EnumFacing targetSide = side.getOpposite();
                actions[side.ordinal()] = AFUtil.isWhiteListTE(te, targetSide)
                        && AFUtil.shouldTryCast(te, targetSide)
                        ? EnergyHandler.getHandler(te, targetSide)
                        : EnergyHandler.SendAction.NOOP;
            }
            actions[side.ordinal()].send(storage, source);
        }
        if (AFConfig.selfCharge()) {
            IEnergyService energy = getMainNode().getGrid().getEnergyService();
            if (energy != null) {
                EnergyHandler.chargeNetwork(energy, storage, source);
            }
        }
    }

    @Override
    public void onChange(EnumFacing side) {
        actions[side.ordinal()] = null;
    }

    private IStorageService getStorage() {
        IManagedGridNode main = getMainNode();
        if (main == null || main.getGrid() == null || !main.isActive()) {
            return null;
        }
        return main.getGrid().getStorageService();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyStorage);
        }
        return super.getCapability(capability, facing);
    }
}
