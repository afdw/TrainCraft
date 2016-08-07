package com.tc.pipe;

import com.tc.ModTrainCraft;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemPipe extends ItemMultiPart {
    public ItemPipe() {
        setUnlocalizedName("tc.pipe");
        setCreativeTab(ModTrainCraft.CREATIVE_TAB);
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        PartPipe partPipeNew = new PartPipe();
        for(EnumFacing facing : EnumFacing.values()) {
            IMultipartContainer multipartContainer = MultipartHelper.getPartContainer(world, pos.offset(facing));
            if(multipartContainer != null) {
                PartPipe[] partPipes = multipartContainer.getParts().stream().filter(part -> part.getClass() == PartPipe.class).toArray(PartPipe[]::new);
                for(PartPipe partPipe : partPipes) {
                    if(!partPipeNew.connections.contains(facing)) {
                        partPipeNew.connections.add(facing);
                    }
                    if(!partPipe.connections.contains(facing.getOpposite())) {
                        partPipe.connections.add(facing.getOpposite());
                    }
                }
            }
        }
        return partPipeNew;
    }
}
