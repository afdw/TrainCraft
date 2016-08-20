package com.tc.lib;

import net.minecraft.client.renderer.VertexBuffer;

public class RenderingUtils {
    public static void drawVertex(VertexBuffer vertexBuffer, double[] pos, int[] color, double[] uvs, int[] lightmap) {
//        pos[0] = Math.max(0, Math.min(1, pos[0]));
//        pos[1] = Math.max(0, Math.min(1, pos[1]));
//        pos[2] = Math.max(0, Math.min(1, pos[2]));
        vertexBuffer.pos(pos[0], pos[1], pos[2]).color(color[0], color[1], color[2], color[3]).tex(uvs[0], uvs[1]).lightmap(lightmap[0], lightmap[1]).endVertex();
    }

    public static void drawCuboid(VertexBuffer vertexBuffer, double[][] points, int[] color, double[][][] uvs, int[][] lightmap) {
        drawVertex(vertexBuffer, new double[]{points[0][0], points[0][1], points[0][2]}, color, uvs[0][0], lightmap[0]);
        drawVertex(vertexBuffer, new double[]{points[1][0], points[1][1], points[1][2]}, color, uvs[0][1], lightmap[0]);
        drawVertex(vertexBuffer, new double[]{points[2][0], points[2][1], points[2][2]}, color, uvs[0][2], lightmap[0]);
        drawVertex(vertexBuffer, new double[]{points[3][0], points[3][1], points[3][2]}, color, uvs[0][3], lightmap[0]);

        drawVertex(vertexBuffer, new double[]{points[4][0], points[4][1], points[4][2]}, color, uvs[1][0], lightmap[1]);
        drawVertex(vertexBuffer, new double[]{points[5][0], points[5][1], points[5][2]}, color, uvs[1][1], lightmap[1]);
        drawVertex(vertexBuffer, new double[]{points[6][0], points[6][1], points[6][2]}, color, uvs[1][2], lightmap[1]);
        drawVertex(vertexBuffer, new double[]{points[7][0], points[7][1], points[7][2]}, color, uvs[1][3], lightmap[1]);

        drawVertex(vertexBuffer, new double[]{points[0][0], points[0][1], points[0][2]}, color, uvs[2][0], lightmap[2]);
        drawVertex(vertexBuffer, new double[]{points[4][0], points[4][1], points[4][2]}, color, uvs[2][1], lightmap[2]);
        drawVertex(vertexBuffer, new double[]{points[7][0], points[7][1], points[7][2]}, color, uvs[2][2], lightmap[2]);
        drawVertex(vertexBuffer, new double[]{points[3][0], points[3][1], points[3][2]}, color, uvs[2][3], lightmap[2]);

        drawVertex(vertexBuffer, new double[]{points[1][0], points[1][1], points[1][2]}, color, uvs[3][0], lightmap[3]);
        drawVertex(vertexBuffer, new double[]{points[5][0], points[5][1], points[5][2]}, color, uvs[3][1], lightmap[3]);
        drawVertex(vertexBuffer, new double[]{points[6][0], points[6][1], points[6][2]}, color, uvs[3][2], lightmap[3]);
        drawVertex(vertexBuffer, new double[]{points[2][0], points[2][1], points[2][2]}, color, uvs[3][3], lightmap[3]);

        drawVertex(vertexBuffer, new double[]{points[0][0], points[0][1], points[0][2]}, color, uvs[4][0], lightmap[4]);
        drawVertex(vertexBuffer, new double[]{points[4][0], points[4][1], points[4][2]}, color, uvs[4][1], lightmap[4]);
        drawVertex(vertexBuffer, new double[]{points[5][0], points[5][1], points[5][2]}, color, uvs[4][2], lightmap[4]);
        drawVertex(vertexBuffer, new double[]{points[1][0], points[1][1], points[1][2]}, color, uvs[4][3], lightmap[4]);

        drawVertex(vertexBuffer, new double[]{points[3][0], points[3][1], points[3][2]}, color, uvs[5][0], lightmap[5]);
        drawVertex(vertexBuffer, new double[]{points[7][0], points[7][1], points[7][2]}, color, uvs[5][1], lightmap[5]);
        drawVertex(vertexBuffer, new double[]{points[6][0], points[6][1], points[6][2]}, color, uvs[5][2], lightmap[5]);
        drawVertex(vertexBuffer, new double[]{points[2][0], points[2][1], points[2][2]}, color, uvs[5][3], lightmap[5]);
    }
}
