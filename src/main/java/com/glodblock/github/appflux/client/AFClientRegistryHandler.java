package com.glodblock.github.appflux.client;

import ae2.api.client.AEKeyRendering;
import com.glodblock.github.appflux.Reference;
import com.glodblock.github.appflux.client.render.FluxKeyRenderHandler;
import com.glodblock.github.appflux.common.AFRegistryHandler;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

public class AFClientRegistryHandler extends AFRegistryHandler {

    @Override
    public void init() {
        super.init();
        AEKeyRendering.register(FluxKeyType.TYPE, FluxKey.class, FluxKeyRenderHandler.INSTANCE);
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
    public static final class Models {

        private Models() {
        }

        @SubscribeEvent
        public static void registerModel(ModelRegistryEvent event) {
            for (Item item : AFRegistryHandler.getItems()) {
                registerItemModel(item);
            }
            for (Block block : AFRegistryHandler.getBlocks()) {
                registerItemModel(Item.getItemFromBlock(block));
            }
        }

        private static void registerItemModel(Item item) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        }

        @SubscribeEvent
        public static void registerSprites(TextureStitchEvent.Pre event) {
            event.getMap().registerSprite(FluxKeyRenderHandler.FE_SPRITE);
        }
    }
}
