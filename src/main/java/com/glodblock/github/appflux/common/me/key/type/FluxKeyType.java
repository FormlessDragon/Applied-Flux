package com.glodblock.github.appflux.common.me.key.type;

import ae2.api.stacks.AEKey;
import ae2.api.stacks.AEKeyType;
import com.glodblock.github.appflux.AppFlux;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.config.AFConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentTranslation;

public class FluxKeyType extends AEKeyType {

    public static final FluxKeyType TYPE = new FluxKeyType();

    private FluxKeyType() {
        super(AppFlux.id("flux"), FluxKey.class, new TextComponentTranslation("appflux.key.flux"));
    }

    @Override
    public AEKey readFromPacket(PacketBuffer input) {
        return FluxKey.of(EnergyType.values()[input.readVarInt()]);
    }

    @Override
    public AEKey loadKeyFromTag(NBTTagCompound tag) {
        return FluxKey.of(EnergyType.valueOf(tag.getString("t")));
    }

    @Override
    public int getAmountPerByte() {
        return AFConfig.getFluxPerByte();
    }

    @Override
    public int getAmountPerOperation() {
        return 1024 * 16;
    }

    @Override
    public boolean isCraftingCpuInsertable() {
        return false;
    }

    @Override
    public String getUnitSymbol() {
        return "FE";
    }

    @Override
    public int getAmountPerUnit() {
        return 1;
    }
}
