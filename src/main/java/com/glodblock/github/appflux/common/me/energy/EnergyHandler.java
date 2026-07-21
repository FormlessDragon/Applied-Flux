package com.glodblock.github.appflux.common.me.energy;

import ae2.api.config.Actionable;
import ae2.api.config.PowerUnit;
import ae2.api.networking.energy.IEnergyService;
import ae2.api.networking.security.IActionSource;
import ae2.api.networking.storage.IStorageService;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.config.AFConfig;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.xmod.mek.MekEnergyHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;

public final class EnergyHandler {

    private EnergyHandler() {
    }

    public static SendAction getHandler(TileEntity te, EnumFacing side) {
        if (Loader.isModLoaded("mekanism")) {
            return MekEnergyHelper.getHandler(te, side);
        }
        IEnergyStorage cap = AFUtil.findCapability(te, side, CapabilityEnergy.ENERGY);
        if (cap != null) {
            return (storage, source) -> sendFE(cap, storage, source);
        }
        return SendAction.NOOP;
    }

    private static void sendFE(IEnergyStorage accepter, IStorageService storage, IActionSource source) {
        int toAdd = accepter.receiveEnergy(AFUtil.clampLong(AFConfig.getFluxAccessorIO()), true);
        if (toAdd <= 0) {
            return;
        }
        long drained = storage.getInventory().extract(FluxKey.of(EnergyType.FE), toAdd, Actionable.MODULATE, source);
        if (drained <= 0) {
            return;
        }
        int accepted = accepter.receiveEnergy(AFUtil.clampLong(drained), false);
        long diff = drained - accepted;
        if (diff > 0) {
            storage.getInventory().insert(FluxKey.of(EnergyType.FE), diff, Actionable.MODULATE, source);
        }
    }

    public static void chargeNetwork(IEnergyService energy, IStorageService storage, IActionSource source) {
        double demandAE = Math.max(0, energy.getEnergyDemand(Integer.MAX_VALUE));
        long demandFE = (long) PowerUnit.AE.convertTo(PowerUnit.FE, demandAE);
        long toDrain = Math.min(demandFE, AFConfig.getFluxAccessorIO());
        long drained = storage.getInventory().extract(FluxKey.of(EnergyType.FE), toDrain, Actionable.MODULATE, source);
        if (drained > 0) {
            energy.injectPower(PowerUnit.FE.convertTo(PowerUnit.AE, drained), Actionable.MODULATE);
        }
    }

    public interface SendAction {
        SendAction NOOP = (storage, source) -> {
        };

        void send(IStorageService storage, IActionSource source);
    }
}
