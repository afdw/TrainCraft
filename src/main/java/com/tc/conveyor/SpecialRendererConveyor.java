package com.tc.conveyor;

import com.tc.lib.RenderingUtils;
import com.tc.lib.Utils;
import mcmultipart.client.multipart.FastMSR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;

public class SpecialRendererConveyor extends FastMSR<PartConveyor> {
    @Override
    public void renderMultipartFast(PartConveyor part, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer vertexBuffer) {
        if(part != null) {
            BlockPos pos = part.getPos();
            int lightCoords = Minecraft.getMinecraft().theWorld.getBlockState(pos).getPackedLightmapCoords(Minecraft.getMinecraft().theWorld, pos);
            int lightX = lightCoords >> 16 & 65535;
            int lightY = lightCoords & 65535;
            TextureAtlasSprite sprite = ModelLoader.White.INSTANCE;
            ResourceLocation textureResourceLocation = new ResourceLocation("tc", "blocks/pipe/base_normal");
            sprite = Utils.getTextureAtlasSprite(textureResourceLocation);
            float minU = sprite.getMinU();
            float maxU = sprite.getMaxU();
            float minV = sprite.getMinV();
            float maxV = sprite.getMaxV();
            double progress = (System.currentTimeMillis() % 1000) / 1000D;
            vertexBuffer.setTranslation(x, y, z);
            int count = 3;
            double size = 1D / count;
            double heightBase = 1 / 16D;
            double heightStart = 0.1 / 16D;
            double heightEnd = 0.3 / 16D;
            for(int i = -1; i < count; i++) {
                double currentStart = (i + progress) * size;
                double startDiff = 1;
                if(i == -1) {
                    startDiff = progress;
                }
                double endDiff = 0;
                if(i == count - 1) {
                    endDiff = progress;
                }
                double startHeight = heightBase + heightEnd + (heightStart - heightEnd) * startDiff;
                double endHeight = heightBase + heightEnd + (heightStart - heightEnd) * endDiff;
                double[][] points;
                if(part.from == part.to.getOpposite()) {
                    EnumFacing facing = part.from;
                    double start = Math.max(currentStart, 0);
                    double end = Math.min(currentStart + size, 1);
                    points = new double[][] {
                            Utils.rotateVertex(new double[] {1 / 16.0, heightBase, start}, facing),
                            Utils.rotateVertex(new double[] {1 / 16.0, startHeight, start}, facing),
                            Utils.rotateVertex(new double[] {1 / 16.0, endHeight, end}, facing),
                            Utils.rotateVertex(new double[] {1 / 16.0, heightBase, end}, facing),

                            Utils.rotateVertex(new double[] {15 / 16.0, heightBase, start}, facing),
                            Utils.rotateVertex(new double[] {15 / 16.0, startHeight, start}, facing),
                            Utils.rotateVertex(new double[] {15 / 16.0, endHeight, end}, facing),
                            Utils.rotateVertex(new double[] {15 / 16.0, heightBase, end}, facing)
                    };
                } else {
                    EnumFacing facing = part.to.getOpposite();
                    double start = Math.max(currentStart, 0) * Math.PI / 2;
                    double end = Math.min(currentStart + size, 1) * Math.PI / 2;
                    boolean rotate = part.to == part.from.rotateYCCW();
                    int add = rotate ? 1 : 0;
                    int multiply = rotate ? -1 : 1;
                    points = new double[][] {
                            Utils.rotateVertex(new double[] {Math.cos(start) * 1 / 16D * multiply + add, heightBase, Math.sin(start) * 1 / 16D}, facing),
                            Utils.rotateVertex(new double[] {Math.cos(start) * 1 / 16D * multiply + add, startHeight, Math.sin(start) * 1 / 16D}, facing),
                            Utils.rotateVertex(new double[] {Math.cos(end) * 1 / 16D * multiply + add, endHeight, Math.sin(end) * 1 / 16D}, facing),
                            Utils.rotateVertex(new double[] {Math.cos(end) * 1 / 16D * multiply + add, heightBase, Math.sin(end) * 1 / 16D}, facing),

                            Utils.rotateVertex(new double[] {Math.cos(start) * 15 / 16D * multiply + add, heightBase, Math.sin(start) * 15 / 16D}, facing),
                            Utils.rotateVertex(new double[] {Math.cos(start) * 15 / 16D * multiply + add, startHeight, Math.sin(start) * 15 / 16D}, facing),
                            Utils.rotateVertex(new double[] {Math.cos(end) * 15 / 16D * multiply + add, endHeight, Math.sin(end) * 15 / 16D}, facing),
                            Utils.rotateVertex(new double[] {Math.cos(end) * 15 / 16D * multiply + add, heightBase, Math.sin(end) * 15 / 16D}, facing)
                    };
                }
                RenderingUtils.drawCuboid(
                        vertexBuffer,
                        points,
                        new int[] {255, 255, 255, 255},
                        new double[][][] {
                                {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}}
                        },
                        new int[][] {
                                {lightX, lightY},
                                {lightX / 2, lightY / 2},
                                {lightX, lightY},
                                {lightX, lightY},
                                {lightX, lightY},
                                {lightX / 2, lightY / 2}
                        }
                );
            }
            vertexBuffer.setTranslation(0, 0, 0);
        }
    }

}
