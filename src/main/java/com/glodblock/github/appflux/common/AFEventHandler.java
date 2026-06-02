package com.glodblock.github.appflux.common;

import ae2.api.parts.IPartHost;
import ae2.helpers.InterfaceLogicHost;
import ae2.helpers.patternprovider.PatternProviderLogicHost;
import com.glodblock.github.appflux.Reference;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class AFEventHandler {

    private AFEventHandler() {
    }

    @SubscribeEvent
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        for (EnumFacing side : event.getNotifiedSides()) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos().offset(side));
            notify(te, side.getOpposite());
        }
    }

    private static void notify(TileEntity te, EnumFacing side) {
        if (te instanceof IPartHost) {
            Object part = ((IPartHost) te).getPart(side);
            if (part instanceof InterfaceLogicHost) {
                ((INeighborListener) ((InterfaceLogicHost) part).getInterfaceLogic()).onChange(side);
            } else if (part instanceof PatternProviderLogicHost) {
                ((INeighborListener) ((PatternProviderLogicHost) part).getLogic()).onChange(side);
            }
            return;
        }
        INeighborListener listener;
        if (te instanceof InterfaceLogicHost) {
            listener = (INeighborListener) ((InterfaceLogicHost) te).getInterfaceLogic();
        } else if (te instanceof PatternProviderLogicHost) {
            listener = (INeighborListener) ((PatternProviderLogicHost) te).getLogic();
        } else {
            return;
        }
        listener.onChange(side);
    }
}
