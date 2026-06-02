package com.glodblock.github.appflux.xmod.mek;

import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import ae2.api.networking.storage.IStorageService;
import com.glodblock.github.appflux.common.me.energy.EnergyHandler;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.config.AFConfig;
import com.glodblock.github.appflux.util.AFUtil;
import mekanism.api.energy.IStrictEnergyAcceptor;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.integration.forgeenergy.ForgeEnergyIntegration;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;

public final class MekEnergyHelper {

    private MekEnergyHelper() {
    }

    public static EnergyHandler.SendAction getHandler(TileEntity te, EnumFacing side) {
        if (!Loader.isModLoaded("mekanism")) {
            return EnergyHandler.SendAction.NOOP;
        }
        IStrictEnergyAcceptor accepter = AFUtil.findCapability(te, side, Capabilities.ENERGY_ACCEPTOR_CAPABILITY);
        if (accepter == null || !accepter.canReceiveEnergy(side)) {
            return EnergyHandler.SendAction.NOOP;
        }
        return (storage, source) -> send(accepter, side, storage, source);
    }

    private static void send(IStrictEnergyAcceptor accepter, EnumFacing side, IStorageService storage, IActionSource source) {
        int request = AFUtil.clampLong(AFConfig.getFluxAccessorIO());
        int toAdd = ForgeEnergyIntegration.toForge(accepter.acceptEnergy(side, ForgeEnergyIntegration.fromForge(request), true));
        if (toAdd <= 0) {
            return;
        }
        long drained = storage.getInventory().extract(FluxKey.of(EnergyType.FE), toAdd, Actionable.MODULATE, source);
        if (drained <= 0) {
            return;
        }
        int accepted = ForgeEnergyIntegration.toForge(
                accepter.acceptEnergy(side, ForgeEnergyIntegration.fromForge(AFUtil.clampLong(drained)), false)
        );
        long diff = drained - accepted;
        if (diff > 0) {
            storage.getInventory().insert(FluxKey.of(EnergyType.FE), diff, Actionable.MODULATE, source);
        }
    }
}
