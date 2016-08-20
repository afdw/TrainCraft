package com.tc.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

public class Utils {
    public static AxisAlignedBB rotateAABB(AxisAlignedBB aabb, EnumFacing facing) {
        if(facing == EnumFacing.DOWN) {
            return new AxisAlignedBB(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
        } else if(facing == EnumFacing.UP) {
            return new AxisAlignedBB(aabb.minX, 1 - aabb.maxY, aabb.minZ, aabb.maxX, 1 - aabb.minY, aabb.maxZ);
        } else if(facing == EnumFacing.NORTH) {
            return new AxisAlignedBB(aabb.minX, aabb.minZ, aabb.minY, aabb.maxX, aabb.maxZ, aabb.maxY);
        } else if(facing == EnumFacing.SOUTH) {
            return new AxisAlignedBB(aabb.minX, aabb.minZ, 1 - aabb.maxY, aabb.maxX, aabb.maxZ, 1 - aabb.minY);
        } else if(facing == EnumFacing.WEST) {
            return new AxisAlignedBB(aabb.minY, aabb.minZ, aabb.minX, aabb.maxY, aabb.maxZ, aabb.maxX);
        } else if(facing == EnumFacing.EAST) {
            return new AxisAlignedBB(1 - aabb.maxY, aabb.minZ, aabb.minX, 1 - aabb.minY, aabb.maxZ, aabb.maxX);
        }
        return aabb;
    }

    public static double[] rotateVertex(double[] pos, EnumFacing facing) {
        if(facing == EnumFacing.NORTH) {
            return new double[]{pos[0], pos[1], pos[2]};
        } else if(facing == EnumFacing.SOUTH) {
            return new double[]{1 - pos[0], pos[1], 1 - pos[2]};
        } else if(facing == EnumFacing.WEST) {
            return new double[]{pos[2], pos[1], 1 - pos[0]};
        } else if(facing == EnumFacing.EAST) {
            return new double[]{1 - pos[2], pos[1], pos[0]};
        }
        return pos;
    }

    public static TextureAtlasSprite getTextureAtlasSprite(ResourceLocation textureResourceLocation) {
        TextureAtlasSprite sprite = null;
        if(textureResourceLocation != null) {
            TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
            if(map.getTextureExtry(textureResourceLocation.toString()) != null) {
                sprite = map.getTextureExtry(textureResourceLocation.toString());
            } else {
                sprite = map.registerSprite(textureResourceLocation);
            }
        }
        return sprite;
    }
}
