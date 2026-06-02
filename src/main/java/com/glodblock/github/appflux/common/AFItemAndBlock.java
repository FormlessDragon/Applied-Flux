package com.glodblock.github.appflux.common;

import ae2.items.materials.MaterialItem;
import ae2.items.parts.PartItem;
import com.glodblock.github.appflux.common.blocks.BlockFluxAccessor;
import com.glodblock.github.appflux.common.items.*;
import com.glodblock.github.appflux.common.parts.PartFluxAccessor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class AFItemAndBlock {

    public static final CreativeTabs TAB = new CreativeTabs("af") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(FE_CELL_1K);
        }
    };

    public static MaterialItem CORE_1K;
    public static MaterialItem CORE_4K;
    public static MaterialItem CORE_16K;
    public static MaterialItem CORE_64K;
    public static MaterialItem CORE_256K;
    public static MaterialItem CORE_1M;
    public static MaterialItem CORE_4M;
    public static MaterialItem CORE_16M;
    public static MaterialItem CORE_64M;
    public static MaterialItem CORE_256M;
    public static MaterialItem CHARGED_REDSTONE;
    public static MaterialItem ENERGY_PROCESSOR_PRINT;
    public static MaterialItem ENERGY_PROCESSOR_PRESS;
    public static MaterialItem ENERGY_PROCESSOR;
    public static MaterialItem REDSTONE_CRYSTAL;
    public static MaterialItem INSULATING_RESIN;
    public static MaterialItem HARDEN_INSULATING_RESIN;
    public static MaterialItem SKY_HARDEN_INSULATING_RESIN;
    public static MaterialItem FE_HOUSING;
    public static MaterialItem MEGA_FE_HOUSING;
    public static ItemFECell FE_CELL_1K;
    public static ItemFECell FE_CELL_4K;
    public static ItemFECell FE_CELL_16K;
    public static ItemFECell FE_CELL_64K;
    public static ItemFECell FE_CELL_256K;
    public static ItemMEGAFECell FE_CELL_1M;
    public static ItemMEGAFECell FE_CELL_4M;
    public static ItemMEGAFECell FE_CELL_16M;
    public static ItemMEGAFECell FE_CELL_64M;
    public static ItemMEGAFECell FE_CELL_256M;
    public static ItemPortableFECell FE_PORTABLE_CELL_1K;
    public static ItemPortableFECell FE_PORTABLE_CELL_4K;
    public static ItemPortableFECell FE_PORTABLE_CELL_16K;
    public static ItemPortableFECell FE_PORTABLE_CELL_64K;
    public static ItemPortableFECell FE_PORTABLE_CELL_256K;
    public static ItemPortableMEGAFECell FE_PORTABLE_CELL_1M;
    public static ItemPortableMEGAFECell FE_PORTABLE_CELL_4M;
    public static ItemPortableMEGAFECell FE_PORTABLE_CELL_16M;
    public static ItemPortableMEGAFECell FE_PORTABLE_CELL_64M;
    public static ItemPortableMEGAFECell FE_PORTABLE_CELL_256M;
    public static ItemCreativeFECell FE_CREATIVE_CELL;
    public static BlockFluxAccessor FLUX_ACCESSOR;
    public static PartItem<PartFluxAccessor> PART_FLUX_ACCESSOR;
    public static ItemInductionCard INDUCTION_CARD;

    private AFItemAndBlock() {
    }

    public static void init() {
        CORE_1K = AFRegistryHandler.item("core_1k", new MaterialItem());
        CORE_4K = AFRegistryHandler.item("core_4k", new MaterialItem());
        CORE_16K = AFRegistryHandler.item("core_16k", new MaterialItem());
        CORE_64K = AFRegistryHandler.item("core_64k", new MaterialItem());
        CORE_256K = AFRegistryHandler.item("core_256k", new MaterialItem());
        CORE_1M = AFRegistryHandler.item("core_1m", new MaterialItem());
        CORE_4M = AFRegistryHandler.item("core_4m", new MaterialItem());
        CORE_16M = AFRegistryHandler.item("core_16m", new MaterialItem());
        CORE_64M = AFRegistryHandler.item("core_64m", new MaterialItem());
        CORE_256M = AFRegistryHandler.item("core_256m", new MaterialItem());
        CHARGED_REDSTONE = AFRegistryHandler.item("charged_redstone", new MaterialItem());
        REDSTONE_CRYSTAL = AFRegistryHandler.item("redstone_crystal", new MaterialItem());
        INSULATING_RESIN = AFRegistryHandler.item("insulating_resin", new MaterialItem());
        HARDEN_INSULATING_RESIN = AFRegistryHandler.item("harden_insulating_resin", new MaterialItem());
        SKY_HARDEN_INSULATING_RESIN = AFRegistryHandler.item("sky_harden_insulating_resin", new MaterialItem());
        ENERGY_PROCESSOR = AFRegistryHandler.item("energy_processor", new MaterialItem());
        ENERGY_PROCESSOR_PRINT = AFRegistryHandler.item("printed_energy_processor", new MaterialItem());
        ENERGY_PROCESSOR_PRESS = AFRegistryHandler.item("energy_processor_press", new MaterialItem());
        FE_HOUSING = AFRegistryHandler.item("fe_cell_housing", new MaterialItem());
        MEGA_FE_HOUSING = AFRegistryHandler.item("mega_fe_cell_housing", new MaterialItem());
        FE_CELL_1K = AFRegistryHandler.item("fe_1k_cell", new ItemFECell(CORE_1K, 1, 0.5));
        FE_CELL_4K = AFRegistryHandler.item("fe_4k_cell", new ItemFECell(CORE_4K, 4, 1.0));
        FE_CELL_16K = AFRegistryHandler.item("fe_16k_cell", new ItemFECell(CORE_16K, 16, 1.5));
        FE_CELL_64K = AFRegistryHandler.item("fe_64k_cell", new ItemFECell(CORE_64K, 64, 2.0));
        FE_CELL_256K = AFRegistryHandler.item("fe_256k_cell", new ItemFECell(CORE_256K, 256, 2.5));
        FE_CELL_1M = AFRegistryHandler.item("fe_1m_cell", new ItemMEGAFECell(CORE_1M, 1024, 3.0));
        FE_CELL_4M = AFRegistryHandler.item("fe_4m_cell", new ItemMEGAFECell(CORE_4M, 4 * 1024, 4.0));
        FE_CELL_16M = AFRegistryHandler.item("fe_16m_cell", new ItemMEGAFECell(CORE_16M, 16 * 1024, 5.0));
        FE_CELL_64M = AFRegistryHandler.item("fe_64m_cell", new ItemMEGAFECell(CORE_64M, 64 * 1024, 6.0));
        FE_CELL_256M = AFRegistryHandler.item("fe_256m_cell", new ItemMEGAFECell(CORE_256M, 256 * 1024, 7.0));
        FE_PORTABLE_CELL_1K = AFRegistryHandler.item("fe_1k_portable_cell", new ItemPortableFECell(1, 0.5, 0xDDDDDD));
        FE_PORTABLE_CELL_4K = AFRegistryHandler.item("fe_4k_portable_cell", new ItemPortableFECell(4, 1.0, 0xDDDDDD));
        FE_PORTABLE_CELL_16K = AFRegistryHandler.item("fe_16k_portable_cell", new ItemPortableFECell(16, 1.5, 0xDDDDDD));
        FE_PORTABLE_CELL_64K = AFRegistryHandler.item("fe_64k_portable_cell", new ItemPortableFECell(64, 2.0, 0xDDDDDD));
        FE_PORTABLE_CELL_256K = AFRegistryHandler.item("fe_256k_portable_cell", new ItemPortableFECell(256, 2.5, 0xDDDDDD));
        FE_PORTABLE_CELL_1M = AFRegistryHandler.item("fe_1m_portable_cell", new ItemPortableMEGAFECell(1024, 3.0, 0xDDDDDD));
        FE_PORTABLE_CELL_4M = AFRegistryHandler.item("fe_4m_portable_cell", new ItemPortableMEGAFECell(4 * 1024, 3.5, 0xDDDDDD));
        FE_PORTABLE_CELL_16M = AFRegistryHandler.item("fe_16m_portable_cell", new ItemPortableMEGAFECell(16 * 1024, 4.0, 0xDDDDDD));
        FE_PORTABLE_CELL_64M = AFRegistryHandler.item("fe_64m_portable_cell", new ItemPortableMEGAFECell(64 * 1024, 4.5, 0xDDDDDD));
        FE_PORTABLE_CELL_256M = AFRegistryHandler.item("fe_256m_portable_cell", new ItemPortableMEGAFECell(256 * 1024, 5.0, 0xDDDDDD));
        FE_CREATIVE_CELL = AFRegistryHandler.item("fe_creative_cell", new ItemCreativeFECell());
        FLUX_ACCESSOR = AFRegistryHandler.block("flux_accessor", new BlockFluxAccessor());
        PART_FLUX_ACCESSOR = AFRegistryHandler.item("part_flux_accessor", new PartItem<>(PartFluxAccessor.class, PartFluxAccessor::new));
        INDUCTION_CARD = AFRegistryHandler.item("induction_card", new ItemInductionCard());
    }
}
