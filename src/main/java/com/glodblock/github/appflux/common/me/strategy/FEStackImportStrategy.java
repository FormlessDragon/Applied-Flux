package com.glodblock.github.appflux.common.me.strategy;

import ae2.api.behaviors.StackImportStrategy;
import ae2.api.behaviors.StackTransferContext;
import ae2.api.config.Actionable;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class FEStackImportStrategy implements StackImportStrategy {

    private final IEnergyStorage storage;

    public FEStackImportStrategy(WorldServer level, BlockPos fromPos, EnumFacing fromSide) {
        TileEntity te = level.getTileEntity(fromPos);
        this.storage = AFUtil.findCapability(te, fromSide, CapabilityEnergy.ENERGY);
    }

    @Override
    public boolean transfer(StackTransferContext context) {
        if (storage == null || !context.isKeyTypeEnabled(FluxKey.of(EnergyType.FE).getType())) {
            return false;
        }
        FluxKey key = FluxKey.of(EnergyType.FE);
        if (!context.isInFilter(key)) {
            return false;
        }
        int simulated = storage.extractEnergy(key.getAmountPerOperation(), true);
        if (simulated <= 0) {
            return false;
        }
        long inserted = context.getInternalStorage().getInventory().insert(key, simulated, Actionable.SIMULATE, context.getActionSource());
        if (inserted <= 0) {
            return false;
        }
        int extracted = storage.extractEnergy(AFUtil.clampLong(inserted), false);
        context.getInternalStorage().getInventory().insert(key, extracted, Actionable.MODULATE, context.getActionSource());
        context.reduceOperationsRemaining(extracted);
        return extracted > 0;
    }
}
