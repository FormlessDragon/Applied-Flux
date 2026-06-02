package com.glodblock.github.appflux.common.items;

import ae2.items.materials.UpgradeCardItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemInductionCard extends UpgradeCardItem {

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advanced) {
        super.addCheckedInformation(stack, world, lines, advanced);
        lines.add(net.minecraft.client.resources.I18n.format("item.appflux.induction_card.tooltip"));
        lines.add(net.minecraft.client.resources.I18n.format("item.appflux.induction_card.tooltip.02"));
    }
}
