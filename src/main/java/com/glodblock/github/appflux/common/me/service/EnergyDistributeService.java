package com.glodblock.github.appflux.common.me.service;

import ae2.api.networking.extensions.GridLogicExtensions;
import ae2.api.networking.GridServices;
import ae2.api.networking.IGridNode;
import ae2.api.networking.IGridService;
import ae2.api.networking.IGridServiceProvider;
import ae2.core.definitions.AEBlocks;
import ae2.core.definitions.AEParts;
import com.glodblock.github.appflux.AppFlux;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class EnergyDistributeService implements IGridService, IGridServiceProvider {

    private final Set<IEnergyDistributor> active = Collections.newSetFromMap(new IdentityHashMap<>());

    public static void register() {
        GridServices.register(EnergyDistributeService.class, EnergyDistributeService.class);
        var registrationId = AppFlux.id("energy_distributor");
        GridLogicExtensions.register(AEBlocks.INTERFACE.item(), registrationId, GridEnergyDistributor::new);
        GridLogicExtensions.register(AEParts.INTERFACE.item(), registrationId, GridEnergyDistributor::new);
        GridLogicExtensions.register(AEBlocks.PATTERN_PROVIDER.item(), registrationId, GridEnergyDistributor::new);
        GridLogicExtensions.register(AEParts.PATTERN_PROVIDER.item(), registrationId, GridEnergyDistributor::new);
    }

    @Override
    public void onServerEndTick() {
        for (IEnergyDistributor distributor : active.toArray(new IEnergyDistributor[0])) {
            if (distributor.isActive()) {
                distributor.distribute();
            }
        }
    }

    @Override
    public void removeNode(IGridNode gridNode) {
        IEnergyDistributor distributor = gridNode.getService(IEnergyDistributor.class);
        if (distributor != null) {
            distributor.setServiceHost(null);
            active.remove(distributor);
        }
    }

    @Override
    public void addNode(IGridNode gridNode, @Nullable net.minecraft.nbt.NBTTagCompound savedData) {
        IEnergyDistributor distributor = gridNode.getService(IEnergyDistributor.class);
        if (distributor != null) {
            distributor.setServiceHost(this);
        }
    }

    public void wake(IEnergyDistributor distributor) {
        active.add(distributor);
    }

    public void sleep(IEnergyDistributor distributor) {
        active.remove(distributor);
    }
}
