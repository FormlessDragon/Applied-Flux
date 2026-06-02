package com.glodblock.github.appflux.mixins;

import ae2.api.config.Actionable;
import ae2.tile.networking.TileCreativeEnergyCell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileCreativeEnergyCell.class, remap = false)
public abstract class MixinCreativeEnergyCell {

    /**
     * @author xinyihl
     * @reason x
     */
    @Overwrite
    public double injectAEPower(double amt, Actionable mode) {
        return amt;
    }
}
