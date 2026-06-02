package com.glodblock.github.appflux.mixins;

import ae2.block.crafting.PatternProviderBlock;
import ae2.helpers.patternprovider.PatternProviderLogicHost;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PatternProviderBlock.class, remap = false)
public abstract class MixinPatternProviderBlock {

    @Inject(method = "neighborChanged", at = @At("TAIL"), remap = false)
    private void appflux$neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, CallbackInfo ci) {
        if (world == null || world.isRemote) {
            return;
        }
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof PatternProviderLogicHost) {
            AFUtil.notifyNeighbor((INeighborListener) ((PatternProviderLogicHost) te).getLogic(), pos, fromPos);
        }
    }
}
