package com.tc.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.animation.FastTESR;

public class SpecialRendererTest extends FastTESR<TileEntityTest> {
    @Override
    public void renderTileEntityFast(TileEntityTest te, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer vertexBuffer) {
        BlockPos pos = te.getPos();
        int lightCoords = Minecraft.getMinecraft().theWorld.getBlockState(pos).getPackedLightmapCoords(Minecraft.getMinecraft().theWorld, pos);
        int lightX = lightCoords >> 16 & 65535;
        int lightY = lightCoords & 65535;
        TextureAtlasSprite sprite = ModelLoader.White.INSTANCE;
        float u = sprite.getInterpolatedU(8);
        float v = sprite.getInterpolatedV(8);
        vertexBuffer.pos(x - 0, y + 1, z + 1).color(255, 255, 255, 255).tex(u, v).lightmap(lightX, lightY).endVertex();
        vertexBuffer.pos(x + 1, y + 1, z + 1).color(255, 255, 255, 255).tex(u, v).lightmap(lightX, lightY).endVertex();
        vertexBuffer.pos(x + 1, y + 1, z - 0).color(255, 255, 255, 255).tex(u, v).lightmap(lightX, lightY).endVertex();
        vertexBuffer.pos(x - 0, y + 1, z - 0).color(255, 255, 255, 255).tex(u, v).lightmap(lightX, lightY).endVertex();
    }
}
