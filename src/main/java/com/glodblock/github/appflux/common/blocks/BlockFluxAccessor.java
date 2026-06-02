package com.glodblock.github.appflux.common.blocks;

import ae2.block.AEBaseTileBlock;
import com.glodblock.github.appflux.common.tileentities.TileFluxAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockFluxAccessor extends AEBaseTileBlock<TileFluxAccessor> {

    public BlockFluxAccessor() {
        super(Material.IRON);
        setHardness(2.2F);
        setResistance(11.0F);
        setTileEntity(TileFluxAccessor.class);
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(net.minecraft.client.resources.I18n.format("block.appflux.flux_accessor.tooltip.1"));
        tooltip.add(net.minecraft.client.resources.I18n.format("block.appflux.flux_accessor.tooltip.2"));
    }

    @Override
    public void neighborChanged(net.minecraft.block.state.IBlockState state, World world, BlockPos pos, net.minecraft.block.Block blockIn, BlockPos fromPos) {
        TileFluxAccessor tile = getTileEntity(world, pos);
        if (tile != null) {
            for (EnumFacing side : EnumFacing.VALUES) {
                tile.onChange(side);
            }
        }
    }
}
