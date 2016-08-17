package com.tc.pipe;

import com.tc.lib.RenderingUtils;
import mcmultipart.client.multipart.FastMSR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

public class SpecialRendererPipe extends FastMSR<PartPipe> {

    @Override
    public void renderMultipartAt(PartPipe part, double x, double y, double z, float partialTicks, int destroyStage) {

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.enableCull();

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        renderMultipartFast(part, x, y, z, partialTicks, destroyStage, buffer);
        buffer.setTranslation(0, 0, 0);
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void renderMultipartFast(PartPipe part, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer vertexBuffer) {
        BlockPos pos = part.getPos();
        int lightCoords = Minecraft.getMinecraft().theWorld.getBlockState(pos).getPackedLightmapCoords(Minecraft.getMinecraft().theWorld, pos);
        int lightX = lightCoords >> 16 & 65535;
        int lightY = lightCoords & 65535;
        TextureAtlasSprite sprite = ModelLoader.White.INSTANCE;
        if(part != null && part.tank != null && part.tank.getFluid() != null && part.tank.getFluid().getFluid() != null) {
            Fluid fluid = part.tank.getFluid().getFluid();
            ResourceLocation fluidResourceLocation = fluid.getStill();
            if(fluidResourceLocation != null) {
                TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
                if(map.getTextureExtry(fluidResourceLocation.toString()) != null) {
                    sprite = map.getTextureExtry(fluidResourceLocation.toString());
                } else {
                    sprite = map.registerSprite(fluidResourceLocation);
                }
            }
            float minU = sprite.getMinU();
            float maxU = sprite.getMaxU();
            float minV = sprite.getMinV();
            float maxV = sprite.getMaxV();
            double pipeStart = 4 / 16D + 0.005, pipeSize = 8 / 16D - 0.01;
            double fluidHeight = (double) part.tank.getFluidAmount() / part.tank.getCapacity();
            RenderingUtils.drawCuboid(vertexBuffer, new double[][]
                    {
                            {x + pipeStart, y + pipeStart, z + pipeStart},
                            {x + pipeStart, y + pipeStart + pipeSize * fluidHeight /*NW*/, z + pipeStart},
                            {x + pipeStart, y + pipeStart + pipeSize * fluidHeight /*SW*/, z + pipeStart + pipeSize},
                            {x + pipeStart, y + pipeStart, z + pipeStart + pipeSize},

                            {x + pipeStart + pipeSize, y + pipeStart, z + pipeStart},
                            {x + pipeStart + pipeSize, y + pipeStart + pipeSize * fluidHeight /*NE*/, z + pipeStart},
                            {x + pipeStart + pipeSize, y + pipeStart + pipeSize * fluidHeight /*SE*/, z + pipeStart + pipeSize},
                            {x + pipeStart + pipeSize, y + pipeStart, z + pipeStart + pipeSize}
                    },
                    new int[]{255, 255, 255, 255}, new double[][][]{
                            {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                            {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                            {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                            {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                            {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                            {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}}
                    }, new int[]{lightX, lightY});
        }
    }
}
