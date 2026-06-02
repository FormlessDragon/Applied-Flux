package com.glodblock.github.appflux.mixins;

import ae2.api.parts.IPartItem;
import ae2.helpers.patternprovider.PatternProviderLogic;
import ae2.parts.AEBasePart;
import ae2.parts.crafting.PatternProviderPart;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PatternProviderPart.class, remap = false)
public abstract class MixinPatternProviderPart extends AEBasePart {

    @Final
    @Shadow
    protected PatternProviderLogic logic;

    protected MixinPatternProviderPart(IPartItem<?> partItem) {
        super(partItem);
    }

    @Inject(method = "onNeighborChanged", at = @At("TAIL"))
    private void appflux$neighborChanged(IBlockAccess world, BlockPos pos, BlockPos neighbor, CallbackInfo ci) {
        EnumFacing side = AFUtil.getBlockDirection(pos, neighbor);
        if (side != null && side == getSide()) {
            ((INeighborListener) logic).onChange(side);
        }
    }
}
