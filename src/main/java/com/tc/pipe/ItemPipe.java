package com.tc.pipe;

import com.tc.ModTrainCraft;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ItemPipe extends ItemMultiPart {
    public ItemPipe() {
        setUnlocalizedName("tc.pipe");
        setHasSubtypes(true);
        setCreativeTab(ModTrainCraft.CREATIVE_TAB);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        for(EnumPipeType pipeType : EnumPipeType.values()) {
            subItems.add(new ItemStack(item, 1, pipeType.ordinal()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + EnumPipeType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        return new PartPipe(EnumPipeType.values()[stack.getMetadata()]);
    }
}
