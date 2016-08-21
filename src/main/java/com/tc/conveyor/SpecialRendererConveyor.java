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
    // t: current time, b: begInnIng value, c: change In value, d: duration
    private double ease(double t, double b, double c, double d) {
        if(t == 0) {
            return b;
        }
        if(t == d) {
            return b + c;
        }
        if((t /= d / 2) < 1) {
            return c / 2 * Math.pow(2, 10 * (t - 1)) + b;
        }
        return c / 2 * (-Math.pow(2, -10 * --t) + 2) + b;
    }

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
            EnumFacing facing = part.direction;
            if(part.conveyorType == EnumConveyorType.UP || part.conveyorType == EnumConveyorType.DOWN) {
                double step = 0.05;
                double oldYScale = 1;
                double oldZScale = 0;
                for(double j = 0; j < 1; j += step) {
                    //noinspection UnnecessaryLocalVariable
                    double start = j;
                    double end = j + step;
                    double startHeight = start < 0.35 ? 0 : ease(start - 0.35, 0, 1, 0.65);
                    double endHeight = end < 0.35 ? 0 : ease(end - 0.35, 0, 1, 0.65);
                    if(part.conveyorType == EnumConveyorType.DOWN) {
                        startHeight = start > 0.65 ? 0 : 1 - ease(start, 0, 1, 0.65);
                        endHeight = end > 0.65 ? 0 : 1 - ease(end, 0, 1, 0.65);
                    }
                    double angle = Math.atan2(endHeight - startHeight, end - start);
                    if(j + step * 2 > 1) {
                        angle = 0;
                    }
                    angle += Math.PI / 2;
                    double zScale = Math.cos(angle);
                    double yScale = Math.sin(angle);
                    for(int i = 0; i < 3; i++) {
                        double xStart = 0;
                        double xEnd = 1;
                        double heightAdd = 0;
                        int[][] lightmap = new int[][] {
                                {(int) (lightX * 0.5), (int) (lightY * 0.5)},
                                {(int) (lightX * 0.5), (int) (lightY * 0.5)},
                                {(int) (lightX * 0.7), (int) (lightY * 0.7)},
                                {(int) (lightX * 0.8), (int) (lightY * 0.8)},
                                {lightX, lightY},
                                {lightX, lightY}
                        };
                        if(i == 0) {
                            xStart = 1 / 16D;
                            xEnd = 15 / 16D;
                        }
                        if(i == 1 || i == 2) {
                            heightAdd = heightBase;
                            if(i == 1) {
                                xEnd = 1 / 16D;
                            } else {
                                xStart = 15 / 16D;
                            }
                            lightmap = new int[][] {
                                    {(int) (lightX * 0.5), (int) (lightY * 0.5)},
                                    {(int) (lightX * 0.5), (int) (lightY * 0.5)},
                                    {(int) (lightX * 0.7), (int) (lightY * 0.7)},
                                    {lightX, lightY},
                                    {lightX, lightY},
                                    {lightX, lightY}
                            };
                        }
                        RenderingUtils.drawCuboid(
                                vertexBuffer,
                                new double[][] {
                                        Utils.rotateVertex(new double[] {xStart, startHeight, start}, facing),
                                        Utils.rotateVertex(new double[] {xStart, (heightAdd + heightBase) * oldYScale + startHeight, (heightAdd + heightBase) * oldZScale + start}, facing),
                                        Utils.rotateVertex(new double[] {xStart, (heightAdd + heightBase) * yScale + endHeight, (heightAdd + heightBase) * zScale + end}, facing),
                                        Utils.rotateVertex(new double[] {xStart, endHeight, end}, facing),

                                        Utils.rotateVertex(new double[] {xEnd, startHeight, start}, facing),
                                        Utils.rotateVertex(new double[] {xEnd, (heightAdd + heightBase) * oldYScale + startHeight, (heightAdd + heightBase) * oldZScale + start}, facing),
                                        Utils.rotateVertex(new double[] {xEnd, (heightAdd + heightBase) * yScale + endHeight, (heightAdd + heightBase) * zScale + end}, facing),
                                        Utils.rotateVertex(new double[] {xEnd, endHeight, end}, facing)
                                },
                                new int[] {255, 255, 255, 255},
                                new double[][][] {
                                        {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                        {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                        {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                        {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                        {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}},
                                        {{minU, minV}, {minU, maxV}, {maxU, maxV}, {maxU, minV}}
                                },
                                lightmap
                        );
                    }
                    oldYScale = yScale;
                    oldZScale = zScale;
                }
            }
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
                if(part.conveyorType == EnumConveyorType.STRAIGHT || part.conveyorType == EnumConveyorType.UP || part.conveyorType == EnumConveyorType.DOWN) {
                    double start = Math.max(currentStart, 0);
                    double end = Math.min(currentStart + size, 1);
                    double heightOffset = 0;
                    if(part.conveyorType == EnumConveyorType.UP) {
                        heightOffset = ease((start + end) / 2, 0, 1, 1);
                    }
                    if(part.conveyorType == EnumConveyorType.DOWN) {
                        heightOffset = 1 - ease((start + end) / 2, 0, 1, 1);
                    }
                    points = new double[][] {
                            Utils.rotateVertex(new double[] {1 / 16.0, heightOffset + heightBase, start}, facing),
                            Utils.rotateVertex(new double[] {1 / 16.0, heightOffset + startHeight, start}, facing),
                            Utils.rotateVertex(new double[] {1 / 16.0, heightOffset + endHeight, end}, facing),
                            Utils.rotateVertex(new double[] {1 / 16.0, heightOffset + heightBase, end}, facing),

                            Utils.rotateVertex(new double[] {15 / 16.0, heightOffset + heightBase, start}, facing),
                            Utils.rotateVertex(new double[] {15 / 16.0, heightOffset + startHeight, start}, facing),
                            Utils.rotateVertex(new double[] {15 / 16.0, heightOffset + endHeight, end}, facing),
                            Utils.rotateVertex(new double[] {15 / 16.0, heightOffset + heightBase, end}, facing)
                    };
                } else {
                    double start = Math.max(currentStart, 0) * Math.PI / 2;
                    double end = Math.min(currentStart + size, 1) * Math.PI / 2;
                    boolean rotate = part.conveyorType == EnumConveyorType.RIGHT;
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
                                {lightX / 2, lightY / 2},
                                {lightX / 2, lightY / 2},
                                {lightX, lightY},
                                {lightX, lightY},
                                {lightX / 2, lightY / 2},
                                {lightX / 2, lightY / 2}
                        }
                );
            }
            vertexBuffer.setTranslation(0, 0, 0);
        }
    }

}
