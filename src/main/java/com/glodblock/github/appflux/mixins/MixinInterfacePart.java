package com.glodblock.github.appflux.mixins;

import ae2.api.parts.IPartItem;
import ae2.helpers.InterfaceLogic;
import ae2.parts.AEBasePart;
import ae2.parts.misc.InterfacePart;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InterfacePart.class)
public abstract class MixinInterfacePart extends AEBasePart {

    @Final
    @Shadow(remap = false)
    private InterfaceLogic logic;

    public MixinInterfacePart(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public void onNeighborChanged(IBlockAccess level, net.minecraft.util.math.BlockPos pos, net.minecraft.util.math.BlockPos neighbor) {
        EnumFacing side = AFUtil.getBlockDirection(pos, neighbor);
        if (side != null && side == getSide()) {
            ((INeighborListener) logic).onChange(side);
        }
    }
}
