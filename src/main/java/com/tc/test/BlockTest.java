package com.tc.test;

import com.tc.ModTrainCraft;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTest extends Block implements ITileEntityProvider {
    public BlockTest() {
        super(Material.ROCK);
        setUnlocalizedName("tc.test");
        setCreativeTab(ModTrainCraft.CREATIVE_TAB);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityTest();
    }
}
