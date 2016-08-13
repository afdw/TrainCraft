package com.tc.conveyor;

import com.tc.ModTrainCraft;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemConveyor extends ItemMultiPart {
    public ItemConveyor() {
        setUnlocalizedName("tc.conveyor");
        setCreativeTab(ModTrainCraft.CREATIVE_TAB);
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        return new PartConveyor(player.getHorizontalFacing(), player.getHorizontalFacing().getOpposite());
    }
}
