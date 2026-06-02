package com.glodblock.github.appflux.common.caps;

import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import ae2.api.networking.storage.IStorageService;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.function.Supplier;

public class NetworkFEPower implements IEnergyStorage {

    private final Supplier<IStorageService> storageSupplier;
    private final IActionSource source;

    public NetworkFEPower(Supplier<IStorageService> storageSupplier, IActionSource source) {
        this.storageSupplier = storageSupplier;
        this.source = source;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive <= 0) {
            return 0;
        }
        IStorageService storage = storageSupplier.get();
        if (storage == null) {
            return 0;
        }
        long inserted = storage.getInventory().insert(
                FluxKey.of(EnergyType.FE),
                maxReceive,
                Actionable.ofSimulate(simulate),
                source
        );
        return AFUtil.clampLong(inserted);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract <= 0) {
            return 0;
        }
        IStorageService storage = storageSupplier.get();
        if (storage == null) {
            return 0;
        }
        long extracted = storage.getInventory().extract(
                FluxKey.of(EnergyType.FE),
                maxExtract,
                Actionable.ofSimulate(simulate),
                source
        );
        return AFUtil.clampLong(extracted);
    }

    @Override
    public int getEnergyStored() {
        IStorageService storage = storageSupplier.get();
        return storage == null ? 0 : AFUtil.clampLong(storage.getCachedInventory().get(FluxKey.of(EnergyType.FE)));
    }

    @Override
    public int getMaxEnergyStored() {
        IStorageService storage = storageSupplier.get();
        if (storage == null) {
            return 0;
        }
        long accepted = storage.getInventory().insert(
                FluxKey.of(EnergyType.FE),
                Long.MAX_VALUE - 1,
                Actionable.SIMULATE,
                source
        );
        return AFUtil.clampLong(accepted);
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
