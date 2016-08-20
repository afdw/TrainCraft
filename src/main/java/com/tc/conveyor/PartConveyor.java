package com.tc.conveyor;

import com.tc.lib.TCPart;
import com.tc.lib.TCProperties;
import mcmultipart.client.multipart.IFastMSRPart;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.PartSlot;
import net.minecraft.block.properties.IProperty;
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

    public EnumFacing from;
    public EnumFacing to;
    public boolean isElevator;

    public PartConveyor() {
    }

    public PartConveyor(EnumFacing from, EnumFacing to, boolean isElevator) {
        this.from = from;
        this.to = to;
        this.isElevator = isElevator;
    }

    @Override
    protected void addProperties(List<IProperty<?>> properties) {
        super.addProperties(properties);
        properties.add(TCProperties.CLOSED_NORTH);
        properties.add(TCProperties.CLOSED_SOUTH);
        properties.add(TCProperties.CLOSED_WEST);
        properties.add(TCProperties.CLOSED_EAST);
        properties.add(TCProperties.CLOSED_NORTH_WEST);
        properties.add(TCProperties.CLOSED_SOUTH_WEST);
        properties.add(TCProperties.CLOSED_NORTH_EAST);
        properties.add(TCProperties.CLOSED_SOUTH_EAST);
    }

    private boolean isSideClosed(EnumFacing side) {
        return side != from && side != to;
    }

    private boolean isCornerClosed(EnumFacing one, EnumFacing two) {
        return isSideClosed(one.getOpposite()) && isSideClosed(two.getOpposite());
//        return (from.getOpposite() == one && to.getOpposite() == two) || (from.getOpposite() == two && to.getOpposite() == one);
    }

    @Override
    public IBlockState getActualState(IBlockState state) {
        return state
                .withProperty(TCProperties.CLOSED_NORTH, isSideClosed(EnumFacing.NORTH))
                .withProperty(TCProperties.CLOSED_SOUTH, isSideClosed(EnumFacing.SOUTH))
                .withProperty(TCProperties.CLOSED_WEST, isSideClosed(EnumFacing.WEST))
                .withProperty(TCProperties.CLOSED_EAST, isSideClosed(EnumFacing.EAST))
                .withProperty(TCProperties.CLOSED_NORTH_WEST, isCornerClosed(EnumFacing.NORTH, EnumFacing.WEST))
                .withProperty(TCProperties.CLOSED_SOUTH_WEST, isCornerClosed(EnumFacing.SOUTH, EnumFacing.WEST))
                .withProperty(TCProperties.CLOSED_NORTH_EAST, isCornerClosed(EnumFacing.NORTH, EnumFacing.EAST))
                .withProperty(TCProperties.CLOSED_SOUTH_EAST, isCornerClosed(EnumFacing.SOUTH, EnumFacing.EAST))
                ;
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        buf.writeInt(from.ordinal());
        buf.writeInt(to.ordinal());
        buf.writeBoolean(isElevator);
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        from = EnumFacing.values()[buf.readInt()];
        to = EnumFacing.values()[buf.readInt()];
        isElevator = buf.readBoolean();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("from", from.ordinal());
        tag.setInteger("to", to.ordinal());
        tag.setBoolean("is_elevator", isElevator);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        from = EnumFacing.values()[tag.getInteger("from")];
        to = EnumFacing.values()[tag.getInteger("to")];
        isElevator = tag.getBoolean("is_elevator");
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
