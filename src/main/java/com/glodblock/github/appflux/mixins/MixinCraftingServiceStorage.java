package com.glodblock.github.appflux.mixins;

import ae2.api.config.Actionable;
import ae2.api.networking.security.IActionSource;
import ae2.api.stacks.AEKey;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "ae2.me.service.helpers.CraftingServiceStorage$1", remap = false)
public class MixinCraftingServiceStorage {

    @Inject(method = "insert", at = @At("HEAD"), cancellable = true)
    private void skipFluxCrafting(AEKey what, long amount, Actionable mode, IActionSource source, CallbackInfoReturnable<Long> cir) {
        if (what instanceof FluxKey) {
            cir.setReturnValue(0L);
        }
    }
}
