package com.glodblock.github.appflux;

import ae2.api.stacks.AEKeyTypes;
import ae2.api.storage.StorageCells;
import ae2.parts.automation.StackWorldBehaviors;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.AFRegistryHandler;
import com.glodblock.github.appflux.common.me.cell.FECellHandler;
import com.glodblock.github.appflux.common.me.cell.FECreativeCellHandler;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import com.glodblock.github.appflux.common.me.service.EnergyDistributeService;
import com.glodblock.github.appflux.common.me.strategy.FEContainerItemStrategy;
import com.glodblock.github.appflux.common.me.strategy.FEExternalStorageStrategy;
import com.glodblock.github.appflux.common.me.strategy.FEStackExportStrategy;
import com.glodblock.github.appflux.common.me.strategy.FEStackImportStrategy;
import com.glodblock.github.appflux.config.AFConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:ae2")
public class AppFlux {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    @SidedProxy(
            modId = Reference.MOD_ID,
            clientSide = "com.glodblock.github.appflux.client.AFClientRegistryHandler",
            serverSide = "com.glodblock.github.appflux.common.AFRegistryHandler"
    )
    public static AFRegistryHandler proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AFConfig.load(event.getSuggestedConfigurationFile());
        AFItemAndBlock.init();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        AEKeyTypes.register(FluxKeyType.TYPE);
        StorageCells.addCellHandler(FECellHandler.HANDLER);
        StorageCells.addCellHandler(FECreativeCellHandler.HANDLER);
        StackWorldBehaviors.registerExternalStorageStrategy(FluxKeyType.TYPE, FEExternalStorageStrategy::new);
        StackWorldBehaviors.registerExportStrategy(FluxKeyType.TYPE, FEStackExportStrategy::new);
        if (AFConfig.pullFE()) {
            StackWorldBehaviors.registerImportStrategy(FluxKeyType.TYPE, FEStackImportStrategy::new);
        }
        FEContainerItemStrategy.register();
        EnergyDistributeService.register();
        proxy.init();
    }

    public static ResourceLocation id(String id) {
        return new ResourceLocation(Reference.MOD_ID, id);
    }
}
