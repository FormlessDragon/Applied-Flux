package com.glodblock.github.appflux.client.render;

import ae2.api.client.AEKeyRenderHandler;
import com.glodblock.github.appflux.AppFlux;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class FluxKeyRenderHandler implements AEKeyRenderHandler<FluxKey> {

    public static final FluxKeyRenderHandler INSTANCE = new FluxKeyRenderHandler();
    public static final ResourceLocation FE_SPRITE = AppFlux.id("energy/fe");

    private FluxKeyRenderHandler() {
    }

    @Override
    public void drawInGui(Minecraft minecraft, int x, int y, FluxKey stack) {
        GlStateManager.pushMatrix();
        try {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            drawSprite(getSprite(minecraft), x, y, 16, 16);
        } finally {
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawOnBlockFace(FluxKey what, float scale, int combinedLight, World level) {
        float x0 = -scale / 2.0F;
        float y0 = -scale / 2.0F;
        float x1 = scale / 2.0F;
        float y1 = scale / 2.0F;

        GlStateManager.pushMatrix();
        try {
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            TextureAtlasSprite sprite = getSprite(minecraft);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(x0, y1, 0.0001D).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            buffer.pos(x1, y1, 0.0001D).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            buffer.pos(x1, y0, 0.0001D).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(x0, y0, 0.0001D).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            tessellator.draw();
        } finally {
            GlStateManager.enableCull();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public ITextComponent getDisplayName(FluxKey stack) {
        return stack.getDisplayName();
    }

    private static TextureAtlasSprite getSprite(Minecraft minecraft) {
        return minecraft.getTextureMapBlocks().getAtlasSprite(FE_SPRITE.toString());
    }

    private static void drawSprite(TextureAtlasSprite sprite, int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0D).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
        buffer.pos(x + width, y + height, 0.0D).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
        buffer.pos(x + width, y, 0.0D).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
        buffer.pos(x, y, 0.0D).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
        tessellator.draw();
    }
}
