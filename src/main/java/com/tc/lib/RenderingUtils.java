package com.tc.lib;

import net.minecraft.client.renderer.VertexBuffer;

public class RenderingUtils {
    public static void drawVertex(VertexBuffer vertexBuffer, double[] point, int[] color, double[] uvs, int[] lightmap) {
        vertexBuffer.pos(point[0], point[1], point[2]).color(color[0], color[1], color[2], color[3]).tex(uvs[0], uvs[1]).lightmap(lightmap[0], lightmap[1]).endVertex();
    }

    public static void drawCuboid(VertexBuffer vertexBuffer, double[][] points, int[] color, double[][][] uvs, int[] lightmap) {
        drawVertex(vertexBuffer, new double[]{points[0][0], points[0][1], points[0][2]}, color, uvs[0][0], lightmap);
        drawVertex(vertexBuffer, new double[]{points[1][0], points[1][1], points[1][2]}, color, uvs[0][1], lightmap);
        drawVertex(vertexBuffer, new double[]{points[2][0], points[2][1], points[2][2]}, color, uvs[0][2], lightmap);
        drawVertex(vertexBuffer, new double[]{points[3][0], points[3][1], points[3][2]}, color, uvs[0][3], lightmap);

        drawVertex(vertexBuffer, new double[]{points[4][0], points[4][1], points[4][2]}, color, uvs[1][0], lightmap);
        drawVertex(vertexBuffer, new double[]{points[5][0], points[5][1], points[5][2]}, color, uvs[1][1], lightmap);
        drawVertex(vertexBuffer, new double[]{points[6][0], points[6][1], points[6][2]}, color, uvs[1][2], lightmap);
        drawVertex(vertexBuffer, new double[]{points[7][0], points[7][1], points[7][2]}, color, uvs[1][3], lightmap);

        drawVertex(vertexBuffer, new double[]{points[0][0], points[0][1], points[0][2]}, color, uvs[2][0], lightmap);
        drawVertex(vertexBuffer, new double[]{points[4][0], points[4][1], points[4][2]}, color, uvs[2][1], lightmap);
        drawVertex(vertexBuffer, new double[]{points[7][0], points[7][1], points[7][2]}, color, uvs[2][2], lightmap);
        drawVertex(vertexBuffer, new double[]{points[3][0], points[3][1], points[3][2]}, color, uvs[2][3], lightmap);

        drawVertex(vertexBuffer, new double[]{points[1][0], points[1][1], points[1][2]}, color, uvs[3][0], lightmap);
        drawVertex(vertexBuffer, new double[]{points[5][0], points[5][1], points[5][2]}, color, uvs[3][1], lightmap);
        drawVertex(vertexBuffer, new double[]{points[6][0], points[6][1], points[6][2]}, color, uvs[3][2], lightmap);
        drawVertex(vertexBuffer, new double[]{points[2][0], points[2][1], points[2][2]}, color, uvs[3][3], lightmap);

        drawVertex(vertexBuffer, new double[]{points[0][0], points[0][1], points[0][2]}, color, uvs[4][0], lightmap);
        drawVertex(vertexBuffer, new double[]{points[4][0], points[4][1], points[4][2]}, color, uvs[4][1], lightmap);
        drawVertex(vertexBuffer, new double[]{points[5][0], points[5][1], points[5][2]}, color, uvs[4][2], lightmap);
        drawVertex(vertexBuffer, new double[]{points[1][0], points[1][1], points[1][2]}, color, uvs[4][3], lightmap);

        drawVertex(vertexBuffer, new double[]{points[3][0], points[3][1], points[3][2]}, color, uvs[5][0], lightmap);
        drawVertex(vertexBuffer, new double[]{points[7][0], points[7][1], points[7][2]}, color, uvs[5][1], lightmap);
        drawVertex(vertexBuffer, new double[]{points[6][0], points[6][1], points[6][2]}, color, uvs[5][2], lightmap);
        drawVertex(vertexBuffer, new double[]{points[2][0], points[2][1], points[2][2]}, color, uvs[5][3], lightmap);
    }
}
