package com.glodblock.github.appflux.util;

import ae2.api.networking.GridHelper;
import ae2.api.networking.IGrid;
import ae2.api.networking.IGridNode;
import ae2.api.networking.IInWorldGridNodeHost;
import ae2.api.parts.IPartHost;
import ae2.api.upgrades.IUpgradeableObject;
import ae2.helpers.InterfaceLogicHost;
import ae2.helpers.patternprovider.PatternProviderLogicHost;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.parts.PartFluxAccessor;
import com.glodblock.github.appflux.common.tileentities.TileFluxAccessor;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import java.util.EnumSet;
import java.util.Set;

public final class AFUtil {

    private AFUtil() {
    }

    public static int clampLong(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    public static <T> T findCapability(TileEntity te, EnumFacing side, Capability<T> capability) {
        if (te != null && te.hasCapability(capability, side)) {
            return te.getCapability(capability, side);
        }
        return null;
    }

    public static IGrid getGrid(TileEntity te, EnumFacing side) {
        if (te instanceof IInWorldGridNodeHost) {
            IGridNode node = ((IInWorldGridNodeHost) te).getGridNode(side);
            return node == null ? null : node.grid();
        }
        if (te != null && te.getWorld() != null) {
            IGridNode node = GridHelper.getExposedNode(te.getWorld(), te.getPos(), side);
            return node == null ? null : node.grid();
        }
        return null;
    }

    public static boolean isWhiteListTE(TileEntity te, EnumFacing side) {
        if (te == null) {
            return false;
        }
        if (te instanceof TileFluxAccessor || te instanceof PatternProviderLogicHost) {
            return false;
        }
        if (te instanceof IPartHost) {
            Object part = ((IPartHost) te).getPart(side);
            return !(part instanceof PartFluxAccessor) && !(part instanceof PatternProviderLogicHost);
        }
        return true;
    }

    public static boolean shouldTryCast(TileEntity te, EnumFacing side) {
        if (te instanceof IUpgradeableObject) {
            return ((IUpgradeableObject) te).isUpgradedWith(AFItemAndBlock.INDUCTION_CARD);
        }
        if (te instanceof IPartHost) {
            Object part = ((IPartHost) te).getPart(side);
            return !(part instanceof IUpgradeableObject) ||
                    ((IUpgradeableObject) part).isUpgradedWith(AFItemAndBlock.INDUCTION_CARD);
        }
        return true;
    }

    public static Set<EnumFacing> getSides(InterfaceLogicHost host) {
        return EnumSet.allOf(EnumFacing.class);
    }

    public static Set<EnumFacing> getSides(PatternProviderLogicHost host) {
        return host.getTargets();
    }

    public static TileEntity neighbor(TileEntity te, EnumFacing side) {
        if (te == null || te.getWorld() == null) {
            return null;
        }
        BlockPos pos = te.getPos().offset(side);
        return te.getWorld().getTileEntity(pos);
    }

    public static EnumFacing getBlockDirection(BlockPos base, BlockPos target) {
        if (base == null || target == null || base.equals(target)) {
            return null;
        }
        for (EnumFacing side : EnumFacing.VALUES) {
            if (base.offset(side).equals(target)) {
                return side;
            }
        }
        return null;
    }

    public static void notifyNeighbor(INeighborListener listener, BlockPos pos, BlockPos neighbor) {
        EnumFacing side = getBlockDirection(pos, neighbor);
        if (side != null) {
            listener.onChange(side);
        }
    }
}
