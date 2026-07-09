package com.glodblock.github.appflux.common;

import ae2.api.client.StorageCellModels;
import ae2.api.parts.PartModels;
import ae2.api.parts.RegisterPartCapabilitiesEvent;
import ae2.api.upgrades.Upgrades;
import ae2.core.definitions.AEBlocks;
import ae2.core.definitions.AEItems;
import ae2.core.definitions.AEParts;
import ae2.core.localization.GuiText;
import com.glodblock.github.appflux.AppFlux;
import com.glodblock.github.appflux.Reference;
import com.glodblock.github.appflux.common.parts.PartFluxAccessor;
import com.glodblock.github.appflux.common.tileentities.TileFluxAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class AFRegistryHandler {

    private static final List<Item> ITEMS = new ArrayList<>();
    private static final List<Block> BLOCKS = new ArrayList<>();

    public void preInit() {
        registerCellModels();
        PartModels.registerModels(PartFluxAccessor.MODEL);
    }

    public void init() {
        registerUpgrades();
    }

    public static <T extends Item> T item(String name, T item) {
        item.setRegistryName(AppFlux.id(name));
        item.setTranslationKey(Reference.MOD_ID + "." + name);
        item.setCreativeTab(AFItemAndBlock.TAB);
        ITEMS.add(item);
        return item;
    }

    public static <T extends Block> T block(String name, T block) {
        block.setRegistryName(AppFlux.id(name));
        block.setTranslationKey(Reference.MOD_ID + "." + name);
        block.setCreativeTab(AFItemAndBlock.TAB);
        BLOCKS.add(block);
        return block;
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static List<Block> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : ITEMS) {
            event.getRegistry().register(item);
        }
        for (Block block : BLOCKS) {
            ItemBlock item = new ItemBlock(block);
            item.setRegistryName(block.getRegistryName());
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : BLOCKS) {
            event.getRegistry().register(block);
        }
        GameRegistry.registerTileEntity(TileFluxAccessor.class, AppFlux.id("flux_accessor"));
    }

    @SubscribeEvent
    public static void registerPartCapabilities(RegisterPartCapabilitiesEvent event) {
        event.register(CapabilityEnergy.ENERGY, (part, side) -> part.getEnergyStorage(), PartFluxAccessor.class);
    }


    @SubscribeEvent
    public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
        GameRegistry.addSmelting(AFItemAndBlock.INSULATING_RESIN, new ItemStack(AFItemAndBlock.HARDEN_INSULATING_RESIN), 0.35F);
    }

    private static void registerCellModels() {
        ResourceLocation cell = AppFlux.id("block/drive/fe_cell");
        ResourceLocation mega = AppFlux.id("block/drive/fe_mega_cell");
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_1K, cell);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_4K, cell);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_16K, cell);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_64K, cell);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_256K, cell);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_1M, mega);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_4M, mega);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_16M, mega);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_64M, mega);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CELL_256M, mega);
        StorageCellModels.registerModel(AFItemAndBlock.FE_CREATIVE_CELL, new ResourceLocation("ae2", "block/drive/cells/creative_cell"));
    }

    private static void registerUpgrades() {
        registerCellUpgrades(
                AFItemAndBlock.FE_CELL_1K, AFItemAndBlock.FE_CELL_4K, AFItemAndBlock.FE_CELL_16K,
                AFItemAndBlock.FE_CELL_64K, AFItemAndBlock.FE_CELL_256K, AFItemAndBlock.FE_CELL_1M,
                AFItemAndBlock.FE_CELL_4M, AFItemAndBlock.FE_CELL_16M, AFItemAndBlock.FE_CELL_64M,
                AFItemAndBlock.FE_CELL_256M
        );
        registerPortableCellUpgrades(
                AFItemAndBlock.FE_PORTABLE_CELL_1K, AFItemAndBlock.FE_PORTABLE_CELL_4K,
                AFItemAndBlock.FE_PORTABLE_CELL_16K, AFItemAndBlock.FE_PORTABLE_CELL_64K,
                AFItemAndBlock.FE_PORTABLE_CELL_256K, AFItemAndBlock.FE_PORTABLE_CELL_1M,
                AFItemAndBlock.FE_PORTABLE_CELL_4M, AFItemAndBlock.FE_PORTABLE_CELL_16M,
                AFItemAndBlock.FE_PORTABLE_CELL_64M, AFItemAndBlock.FE_PORTABLE_CELL_256M
        );
        Upgrades.add(AFItemAndBlock.INDUCTION_CARD, AEBlocks.INTERFACE.asItem(), 1, GuiText.Interface.getTranslationKey());
        Upgrades.add(AFItemAndBlock.INDUCTION_CARD, AEParts.INTERFACE.asItem(), 1, GuiText.Interface.getTranslationKey());
        Upgrades.add(AFItemAndBlock.INDUCTION_CARD, AEBlocks.PATTERN_PROVIDER.asItem(), 1, "group.pattern_provider.name");
        Upgrades.add(AFItemAndBlock.INDUCTION_CARD, AEParts.PATTERN_PROVIDER.asItem(), 1, "group.pattern_provider.name");
    }

    private static void registerCellUpgrades(Item... cells) {
        for (Item cell : cells) {
            Upgrades.add(AEItems.VOID_CARD.asItem(), cell, 1, GuiText.StorageCells.getTranslationKey());
        }
    }

    private static void registerPortableCellUpgrades(Item... cells) {
        for (Item cell : cells) {
            Upgrades.add(AEItems.VOID_CARD.asItem(), cell, 1, GuiText.PortableCells.getTranslationKey());
            Upgrades.add(AEItems.ENERGY_CARD.asItem(), cell, 2, GuiText.PortableCells.getTranslationKey());
            Upgrades.add(AFItemAndBlock.INDUCTION_CARD, cell, 1, "group.fe_portable_cells.name");
        }
    }
}
