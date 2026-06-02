package com.glodblock.github.appflux.common.parts;

import ae2.api.networking.GridFlags;
import ae2.api.networking.IGridNode;
import ae2.api.networking.IGridNodeListener;
import ae2.api.networking.IManagedGridNode;
import ae2.api.networking.energy.IEnergyService;
import ae2.api.networking.security.IActionSource;
import ae2.api.networking.storage.IStorageService;
import ae2.api.networking.ticking.IGridTickable;
import ae2.api.networking.ticking.TickRateModulation;
import ae2.api.networking.ticking.TickingRequest;
import ae2.api.parts.IPartCollisionHelper;
import ae2.api.parts.IPartItem;
import ae2.api.parts.IPartModel;
import ae2.api.util.AECableType;
import ae2.parts.AEBasePart;
import ae2.parts.PartModel;
import com.glodblock.github.appflux.AppFlux;
import com.glodblock.github.appflux.common.caps.NetworkFEPower;
import com.glodblock.github.appflux.common.me.energy.EnergyHandler;
import com.glodblock.github.appflux.config.AFConfig;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;

public class PartFluxAccessor extends AEBasePart implements INeighborListener, IGridTickable {

    public static final ResourceLocation MODEL = AppFlux.id("part/flux_accessor");
    private static final PartModel PART_MODEL = new PartModel(MODEL);
    private final EnergyHandler.SendAction[] actions = new EnergyHandler.SendAction[6];
    private final IActionSource source = IActionSource.ofMachine(this);
    private final IEnergyStorage energyStorage = new NetworkFEPower(this::getStorage, source);

    public PartFluxAccessor(IPartItem<?> partItem) {
        super(partItem);
        getMainNode().setFlags(GridFlags.REQUIRE_CHANNEL).addService(IGridTickable.class, this);
    }

    @Override
    public void getBoxes(IPartCollisionHelper bch) {
        bch.addBox(5, 5, 12, 11, 11, 14);
        bch.addBox(3, 3, 14, 13, 13, 16);
    }

    @Override
    public float getCableConnectionLength(AECableType cable) {
        return 4;
    }

    @Override
    public void onNeighborChanged(net.minecraft.world.IBlockAccess world, net.minecraft.util.math.BlockPos pos, net.minecraft.util.math.BlockPos neighbor) {
        for (EnumFacing side : EnumFacing.VALUES) {
            onChange(side);
        }
    }

    @Override
    public void onChange(EnumFacing side) {
        actions[side.ordinal()] = null;
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        tickEnergy();
    }

    @Override
    public IPartModel getStaticModels() {
        return PART_MODEL;
    }

    private void tickEnergy() {
        IManagedGridNode main = getMainNode();
        if (isClientSide() || main == null || main.getGrid() == null || !main.isActive()) {
            return;
        }
        IStorageService storage = getStorage();
        if (storage == null) {
            return;
        }
        EnumFacing side = getSide();
        TileEntity te = AFUtil.neighbor(getTileEntity(), side);
        if (actions[side.ordinal()] == null) {
            EnumFacing targetSide = side.getOpposite();
            actions[side.ordinal()] = AFUtil.isWhiteListTE(te, targetSide)
                    && AFUtil.shouldTryCast(te, targetSide)
                    ? EnergyHandler.getHandler(te, targetSide)
                    : EnergyHandler.SendAction.NOOP;
        }
        actions[side.ordinal()].send(storage, source);
        if (AFConfig.selfCharge()) {
            IEnergyService energy = main.getGrid().getEnergyService();
            if (energy != null) {
                EnergyHandler.chargeNetwork(energy, storage, source);
            }
        }
    }

    private IStorageService getStorage() {
        IManagedGridNode main = getMainNode();
        if (main == null || main.getGrid() == null || !main.isActive()) {
            return null;
        }
        return main.getGrid().getStorageService();
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode node) {
        return new TickingRequest(1, 20, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        tickEnergy();
        return TickRateModulation.SAME;
    }
}
