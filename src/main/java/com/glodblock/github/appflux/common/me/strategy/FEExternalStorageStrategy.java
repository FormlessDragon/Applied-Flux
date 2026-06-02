package com.glodblock.github.appflux.common.me.strategy;

import ae2.api.behaviors.ExternalStorageStrategy;
import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import ae2.api.stacks.AEKey;
import ae2.api.stacks.KeyCounter;
import ae2.api.storage.MEStorage;
import ae2.core.localization.GuiText;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import com.glodblock.github.appflux.util.AFUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class FEExternalStorageStrategy implements ExternalStorageStrategy {

    private final WorldServer level;
    private final BlockPos fromPos;
    private final EnumFacing fromSide;

    public FEExternalStorageStrategy(WorldServer level, BlockPos fromPos, EnumFacing fromSide) {
        this.level = level;
        this.fromPos = fromPos;
        this.fromSide = fromSide;
    }

    @Override
    public MEStorage createWrapper(boolean extractableOnly, Runnable callback) {
        TileEntity te = level.getTileEntity(fromPos);
        IEnergyStorage storage = AFUtil.findCapability(te, fromSide, CapabilityEnergy.ENERGY);
        return storage == null ? null : new FEStorageWrapper(storage, callback);
    }

    private record FEStorageWrapper(IEnergyStorage storage, Runnable callback) implements MEStorage {
        @Override
        public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
            if (!FluxKey.of(EnergyType.FE).equals(what)) {
                return 0;
            }
            int in = storage.receiveEnergy(AFUtil.clampLong(amount), mode.isSimulate());
            if (in > 0 && mode == Actionable.MODULATE) {
                callback.run();
            }
            return in;
        }

        @Override
        public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
            if (!FluxKey.of(EnergyType.FE).equals(what)) {
                return 0;
            }
            int out = storage.extractEnergy(AFUtil.clampLong(amount), mode.isSimulate());
            if (out > 0 && mode == Actionable.MODULATE) {
                callback.run();
            }
            return out;
        }

        @Override
        public void getAvailableStacks(KeyCounter out) {
            int stored = storage.getEnergyStored();
            if (stored > 0) {
                out.add(FluxKey.of(EnergyType.FE), stored);
            }
        }

        @Override
        public ITextComponent getDescription() {
            return new TextComponentTranslation(GuiText.ExternalStorage.getTranslationKey(), FluxKeyType.TYPE.getDescription());
        }
    }
}
