package com.glodblock.github.appflux.common.me.cell;

import ae2.api.stacks.GenericStack;
import ae2.api.storage.cells.ICellHandler;
import ae2.api.storage.cells.ISaveProvider;
import ae2.api.storage.cells.StorageCell;
import ae2.items.storage.StorageCellTooltipComponent;
import com.glodblock.github.appflux.api.IFluxCell;
import com.glodblock.github.appflux.common.items.ItemCreativeFECell;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FECellHandler implements ICellHandler {

    public static final FECellHandler HANDLER = new FECellHandler();

    @Override
    public boolean isCell(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IFluxCell && !(stack.getItem() instanceof ItemCreativeFECell);
    }

    @Override
    public StorageCell getCellInventory(ItemStack stack, @Nullable ISaveProvider container) {
        return isCell(stack) ? new FECellInventory((IFluxCell) stack.getItem(), stack, container) : null;
    }

    public void addCellInformationToTooltip(ItemStack stack, List<String> lines) {
        StorageCell cell = getCellInventory(stack, null);
        if (cell instanceof FluxCellInventory inv) {
            lines.add(String.format("%,d / %,d FE", inv.getStoredEnergy(), inv.getMaxEnergy()));
            lines.add(String.format("%,d / %,d Bytes", inv.getUsedBytes(), inv.getTotalBytes()));
        }
    }

    public Optional<StorageCellTooltipComponent> getTooltipImage(ItemStack stack) {
        StorageCell cell = getCellInventory(stack, null);
        if (cell instanceof FluxCellInventory inv) {
            GenericStack content = new GenericStack(FluxKey.of(EnergyType.FE), inv.getStoredEnergy());
            return Optional.of(new StorageCellTooltipComponent(Collections.emptyList(), Collections.singletonList(content), false, true));
        }
        return Optional.empty();
    }
}
