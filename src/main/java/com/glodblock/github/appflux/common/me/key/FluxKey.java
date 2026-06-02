package com.glodblock.github.appflux.common.me.key;

import ae2.api.stacks.AEKey;
import ae2.api.stacks.AEKeyType;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;

public class FluxKey extends AEKey {

    private final EnergyType type;

    private FluxKey(EnergyType type) {
        this.type = type;
    }

    public static FluxKey of(EnergyType type) {
        return type == null ? null : new FluxKey(type);
    }

    public EnergyType getEnergyType() {
        return type;
    }

    @Override
    public AEKeyType getType() {
        return FluxKeyType.TYPE;
    }

    @Override
    public AEKey dropSecondary() {
        return this;
    }

    @Override
    public NBTTagCompound toTag() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("t", type.name());
        return tag;
    }

    @Override
    public Object getPrimaryKey() {
        return type;
    }

    @Override
    public String getModId() {
        return type.from();
    }

    @Override
    public ResourceLocation getId() {
        return type.id();
    }

    @Override
    public void writeToPacket(PacketBuffer data) {
        data.writeVarInt(type.ordinal());
    }

    @Override
    public Object getReadOnlyStack() {
        return this;
    }

    @Override
    protected ITextComponent computeDisplayName() {
        return type.translate();
    }

    @Override
    public void addDrops(long amount, List<ItemStack> drops, World level, BlockPos pos) {
    }

    @Override
    public boolean isTagged(String tag) {
        return false;
    }

    @Override
    public NBTBase get(String key) {
        return null;
    }

    @Override
    public boolean hasComponents() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof FluxKey && ((FluxKey) o).type == type;
    }

    @Override
    public int hashCode() {
        return type.ordinal();
    }

    @Override
    public String toString() {
        return "FluxKey{" + type.name() + "}";
    }
}
