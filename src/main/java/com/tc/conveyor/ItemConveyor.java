package com.tc.conveyor;

import com.tc.ModTrainCraft;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ItemConveyor extends ItemMultiPart {
    public ItemConveyor() {
        setUnlocalizedName("tc.conveyor");
        setHasSubtypes(true);
        setCreativeTab(ModTrainCraft.CREATIVE_TAB);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        for(EnumConveyorType conveyorType : EnumConveyorType.values()) {
            subItems.add(new ItemStack(item, 1, conveyorType.ordinal()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + EnumConveyorType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(player.isSneaking()) {
            ItemStack copy = itemStack.copy();
            copy.setItemDamage((copy.getItemDamage() + 1) % EnumConveyorType.values().length);
            return new ActionResult<>(EnumActionResult.SUCCESS, copy);
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        return new PartConveyor(EnumConveyorType.values()[stack.getMetadata()], player.getHorizontalFacing().getOpposite());
    }
}
