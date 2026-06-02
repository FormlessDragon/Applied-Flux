package com.glodblock.github.appflux.common.me.service;

import ae2.api.networking.IGridNodeService;

public interface IEnergyDistributor extends IGridNodeService {
    void distribute();

    boolean isActive();

    void setServiceHost(EnergyDistributeService service);
}
