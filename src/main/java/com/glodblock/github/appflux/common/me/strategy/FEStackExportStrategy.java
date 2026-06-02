package com.glodblock.github.appflux.common.me.strategy;

import ae2.api.behaviors.StackExportStrategy;
import ae2.api.behaviors.StackTransferContext;
import ae2.api.config.Actionable;
import ae2.api.stacks.AEKey;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class FEStackExportStrategy implements StackExportStrategy {

    private final IEnergyStorage storage;

    public FEStackExportStrategy(WorldServer level, BlockPos fromPos, EnumFacing fromSide) {
        TileEntity te = level.getTileEntity(fromPos);
        this.storage = AFUtil.findCapability(te, fromSide, CapabilityEnergy.ENERGY);
    }

    @Override
    public long transfer(StackTransferContext context, AEKey what, long amount) {
        long pushed = push(what, amount, Actionable.SIMULATE);
        if (pushed <= 0) {
            return 0;
        }
        long extracted = context.getInternalStorage().getInventory().extract(what, pushed, Actionable.MODULATE, context.getActionSource());
        return push(what, extracted, Actionable.MODULATE);
    }

    @Override
    public long push(AEKey what, long amount, Actionable mode) {
        if (storage == null || !FluxKey.of(EnergyType.FE).equals(what)) {
            return 0;
        }
        return storage.receiveEnergy(AFUtil.clampLong(amount), mode.isSimulate());
    }
}
