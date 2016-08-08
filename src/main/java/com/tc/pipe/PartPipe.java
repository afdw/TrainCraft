package com.tc.pipe;

import com.tc.lib.TCPart;
import com.tc.lib.TCProperties;
import com.tc.lib.Utils;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.List;

public class PartPipe extends TCPart implements INormallyOccludingPart, ISlottedPart {
    public static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(4.0 / 16.0, 4.0 / 16.0, 4.0 / 16.0, 12.0 / 16.0, 12.0 / 16.0, 12.0 / 16.0);
    public static final AxisAlignedBB CONNECTION_AABB = new AxisAlignedBB(2.0 / 16.0, 0.0 / 16.0, 2.0 / 16.0, 14.0 / 16.0, 4.0 / 16.0, 14.0 / 16.0);

    public BitSet connections = new BitSet(6);

    @Override
    protected void addProperties(List<IProperty<?>> properties) {
        super.addProperties(properties);
        properties.add(TCProperties.CONNECTED_DOWN);
        properties.add(TCProperties.CONNECTED_UP);
        properties.add(TCProperties.CONNECTED_NORTH);
        properties.add(TCProperties.CONNECTED_SOUTH);
        properties.add(TCProperties.CONNECTED_WEST);
        properties.add(TCProperties.CONNECTED_EAST);
    }

    @Override
    public IBlockState getActualState(IBlockState state) {
        return state
                .withProperty(TCProperties.CONNECTED_DOWN, connections.get(EnumFacing.DOWN.ordinal()))
                .withProperty(TCProperties.CONNECTED_UP, connections.get(EnumFacing.UP.ordinal()))
                .withProperty(TCProperties.CONNECTED_NORTH, connections.get(EnumFacing.NORTH.ordinal()))
                .withProperty(TCProperties.CONNECTED_SOUTH, connections.get(EnumFacing.SOUTH.ordinal()))
                .withProperty(TCProperties.CONNECTED_WEST, connections.get(EnumFacing.WEST.ordinal()))
                .withProperty(TCProperties.CONNECTED_EAST, connections.get(EnumFacing.EAST.ordinal()))
                ;
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        buf.writeLongArray(connections.toLongArray());
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        connections = BitSet.valueOf(buf.readLongArray(new long[6]));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByteArray("connections", connections.toByteArray());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        connections = BitSet.valueOf(tag.getByteArray("connections"));
    }

    @Override
    public void onAdded() {
        for(EnumFacing facing : EnumFacing.values()) {
            IMultipartContainer multipartContainer = MultipartHelper.getPartContainer(getWorld(), getPos().offset(facing));
            if(multipartContainer != null) {
                ISlottedPart slottedPart = multipartContainer.getPartInSlot(PartSlot.CENTER);
                if(slottedPart != null && slottedPart instanceof PartPipe) {
                    PartPipe partPipe = (PartPipe) slottedPart;
                    connections.set(facing.ordinal());
                    partPipe.connections.set(facing.getOpposite().ordinal());
                }
            }
        }
    }

    @Override
    public void onRemoved() {
        for(EnumFacing facing : EnumFacing.values()) {
            if(!connections.get(facing.ordinal())) {
                continue;
            }
            IMultipartContainer multipartContainer = MultipartHelper.getPartContainer(getWorld(), getPos().offset(facing));
            if(multipartContainer != null) {
                ISlottedPart slottedPart = multipartContainer.getPartInSlot(PartSlot.CENTER);
                if(slottedPart != null && slottedPart instanceof PartPipe) {
                    PartPipe partPipe = (PartPipe) slottedPart;
                    partPipe.connections.clear(facing.getOpposite().ordinal());
                }
            }
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
        if(hit.subHit == -1) {
            return false;
        }
        try {
            if(heldItem.getItem() instanceof ItemPliers) {
                player.swingArm(hand);
                int i = 0;
                for(EnumFacing facing : EnumFacing.values()) {
                    if(!connections.get(facing.ordinal())) {
                        continue;
                    }
                    if(i == hit.subHit) {
                        connections.clear(facing.ordinal());
                        IMultipartContainer multipartContainer = MultipartHelper.getPartContainer(getWorld(), getPos().offset(facing));
                        if(multipartContainer != null) {
                            ISlottedPart slottedPart = multipartContainer.getPartInSlot(PartSlot.CENTER);
                            if(slottedPart != null && slottedPart instanceof PartPipe) {
                                PartPipe partPipe = (PartPipe) slottedPart;
                                partPipe.connections.clear(facing.getOpposite().ordinal());
                            }
                        }
                    }
                    i++;
                }
                markDirty();
                sendUpdatePacket();
            }
            return true;
        } catch(Exception e) {
            return super.onActivated(player, hand, heldItem, hit);
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        if(BASE_AABB.intersectsWith(mask)) {
            list.add(BASE_AABB);
        }
        for(EnumFacing facing : EnumFacing.values()) {
            if(!connections.get(facing.ordinal())) {
                continue;
            }
            if(Utils.rotateAABB(CONNECTION_AABB, facing).intersectsWith(mask)) {
                list.add(Utils.rotateAABB(CONNECTION_AABB, facing));
            }
        }
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
//        AxisAlignedBB aabb = BASE_AABB;
//        for(EnumFacing connection : connections) {
//            aabb = aabb.union(Utils.rotateAABB(CONNECTION_AABB, connection));
//        }
//        list.add(aabb);
        list.add(BASE_AABB);
        for(EnumFacing facing : EnumFacing.values()) {
            if(!connections.get(facing.ordinal())) {
                continue;
            }
            list.add(Utils.rotateAABB(CONNECTION_AABB, facing));
        }
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        addSelectionBoxes(list);
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.CENTER);
    }
}
