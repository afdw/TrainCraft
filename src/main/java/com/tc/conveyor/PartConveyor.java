package com.tc.conveyor;

import com.tc.lib.TCPart;
import com.tc.lib.TCProperties;
import mcmultipart.client.multipart.IFastMSRPart;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.PartSlot;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;
import java.util.List;

public class PartConveyor extends TCPart implements INormallyOccludingPart, ISlottedPart, IFastMSRPart {
    public static final AxisAlignedBB AABB = new AxisAlignedBB(0.0 / 16.0, 0.0 / 16.0, 0.0 / 16.0, 16.0 / 16.0, 2.0 / 16.0, 16.0 / 16.0);

    public EnumConveyorType conveyorType = EnumConveyorType.STRAIGHT;
    public EnumFacing direction = EnumFacing.NORTH;

    public PartConveyor() {
    }

    public PartConveyor(EnumConveyorType conveyorType, EnumFacing direction) {
        this.conveyorType = conveyorType;
        this.direction = direction;
    }

    @Override
    protected void addProperties(List<IProperty<?>> properties) {
        super.addProperties(properties);
        properties.add(TCProperties.CONVEYOR_TYPE);
        properties.add(TCProperties.DIRECTION);
    }

    @Override
    public IBlockState getActualState(IBlockState state) {
        return state
                .withProperty(TCProperties.CONVEYOR_TYPE, conveyorType)
                .withProperty(TCProperties.DIRECTION, direction)
                ;
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        buf.writeInt(conveyorType.ordinal());
        buf.writeInt(direction.ordinal());
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        conveyorType = EnumConveyorType.values()[buf.readInt()];
        direction = EnumFacing.values()[buf.readInt()];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("conveyor_type", conveyorType.ordinal());
        tag.setInteger("direction", direction.ordinal());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        conveyorType = EnumConveyorType.values()[tag.getInteger("conveyor_type")];
        direction = EnumFacing.values()[tag.getInteger("direction")];
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(AABB);
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        if(mask.intersectsWith(AABB)) {
            list.add(AABB);
        }
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        list.add(AABB);
    }

    private PartSlot getPartSlot() {
        return PartSlot.DOWN;
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(getPartSlot());
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }
}
