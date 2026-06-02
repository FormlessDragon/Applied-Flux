package com.glodblock.github.appflux.mixins;

import ae2.api.networking.IManagedGridNode;
import ae2.api.networking.security.IActionSource;
import ae2.api.networking.storage.IStorageService;
import ae2.api.upgrades.IUpgradeInventory;
import ae2.api.upgrades.UpgradeInventories;
import ae2.helpers.InterfaceLogic;
import ae2.helpers.InterfaceLogicHost;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.me.energy.EnergyHandler;
import com.glodblock.github.appflux.common.me.service.EnergyDistributeService;
import com.glodblock.github.appflux.common.me.service.IEnergyDistributor;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Set;

@Mixin(value = InterfaceLogic.class, remap = false)
public abstract class MixinInterfaceLogic implements IEnergyDistributor, INeighborListener {

    @Unique
    private Set<EnumFacing> af$sides = java.util.EnumSet.allOf(EnumFacing.class);
    @Unique
    private EnergyDistributeService af$service;
    @Unique
    private final EnergyHandler.SendAction[] af$actions = new EnergyHandler.SendAction[6];

    @Final
    @Mutable
    @Shadow
    private IUpgradeInventory upgrades;
    @Final
    @Shadow
    protected InterfaceLogicHost host;
    @Final
    @Shadow
    protected IActionSource actionSource;
    @Final
    @Shadow
    protected IManagedGridNode mainNode;

    @Shadow
    protected abstract void onUpgradesChanged();

    @Inject(method = "<init>(Lae2/api/networking/IManagedGridNode;Lae2/helpers/InterfaceLogicHost;Lnet/minecraft/item/Item;I)V", at = @At("TAIL"))
    private void appflux$init(IManagedGridNode gridNode, InterfaceLogicHost host, Item item, int slots, CallbackInfo ci) {
        this.upgrades = UpgradeInventories.forMachine(item, 2, this::onUpgradesChanged);
        this.mainNode.addService(IEnergyDistributor.class, this);
    }

    @Override
    public void distribute() {
        IStorageService storage = af$getStorage();
        TileEntity self = host.getTileEntity();
        if (storage == null || self == null || self.getWorld() == null) {
            return;
        }
        for (EnumFacing side : af$sides) {
            if (af$actions[side.ordinal()] == null) {
                TileEntity te = AFUtil.neighbor(self, side);
                EnumFacing targetSide = side.getOpposite();
                af$actions[side.ordinal()] = te != null
                        && AFUtil.getGrid(te, targetSide) != mainNode.getGrid()
                        && AFUtil.isWhiteListTE(te, targetSide)
                        && AFUtil.shouldTryCast(te, targetSide)
                        ? EnergyHandler.getHandler(te, targetSide)
                        : EnergyHandler.SendAction.NOOP;
            }
            af$actions[side.ordinal()].send(storage, actionSource);
        }
    }

    @Unique
    private IStorageService af$getStorage() {
        return mainNode.getGrid() == null ? null : mainNode.getGrid().getStorageService();
    }

    @Override
    public boolean isActive() {
        return mainNode.isActive();
    }

    @Override
    public void setServiceHost(@Nullable EnergyDistributeService service) {
        af$service = service;
        af$sides = AFUtil.getSides(host);
        af$updateSleep();
    }

    @Override
    public void onChange(EnumFacing side) {
        af$actions[side.ordinal()] = null;
    }

    @Unique
    private void af$updateSleep() {
        if (af$service != null) {
            if (upgrades.isInstalled(AFItemAndBlock.INDUCTION_CARD)) {
                af$service.wake(this);
            } else {
                af$service.sleep(this);
            }
        }
    }

    @Inject(method = "onUpgradesChanged", at = @At("TAIL"))
    private void appflux$onUpgradesChanged(CallbackInfo ci) {
        af$updateSleep();
    }
}
