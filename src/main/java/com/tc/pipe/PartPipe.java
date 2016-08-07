package com.tc.pipe;

import com.tc.lib.TCPart;
import com.tc.lib.TCProperties;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.Multipart;
import mcmultipart.multipart.MultipartHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class PartPipe extends TCPart implements INormallyOccludingPart {
    public List<EnumFacing> connections = new ArrayList<>(6);

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
                .withProperty(TCProperties.CONNECTED_DOWN, connections.contains(EnumFacing.DOWN))
                .withProperty(TCProperties.CONNECTED_UP, connections.contains(EnumFacing.UP))
                .withProperty(TCProperties.CONNECTED_NORTH, connections.contains(EnumFacing.NORTH))
                .withProperty(TCProperties.CONNECTED_SOUTH, connections.contains(EnumFacing.SOUTH))
                .withProperty(TCProperties.CONNECTED_WEST, connections.contains(EnumFacing.WEST))
                .withProperty(TCProperties.CONNECTED_EAST, connections.contains(EnumFacing.EAST))
                ;
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        buf.writeInt(connections.size());
        for(EnumFacing connection : connections) {
            buf.writeInt(connection.ordinal());
        }
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        int count = buf.readInt();
        for(int i = 0; i < count; i++) {
            connections.add(EnumFacing.values()[buf.readInt()]);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        int[] connectionsArray = new int[connections.size()];
        for(int i = 0; i < connections.size(); i++) {
            EnumFacing connection = connections.get(i);
            connectionsArray[i] = connection.ordinal();
        }
        tag.setIntArray("connections", connectionsArray);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        int[] connectionsArray = tag.getIntArray("connections");
        for(int connectionIndex : connectionsArray) {
            connections.add(EnumFacing.values()[connectionIndex]);
        }
    }

    @Override
    public void onRemoved() {
        for(EnumFacing connection : connections) {
            IMultipartContainer multipartContainer = MultipartHelper.getPartContainer(getWorld(), getPos().offset(connection));
            if(multipartContainer != null) {
                PartPipe[] partPipes = multipartContainer.getParts().stream().filter(part -> part.getClass() == PartPipe.class).toArray(PartPipe[]::new);
                for(PartPipe partPipe : partPipes) {
                    if(partPipe.connections.contains(connection.getOpposite())) {
                        partPipe.connections.remove(connection.getOpposite());
                    }
                }
            }
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        if(Multipart.DEFAULT_RENDER_BOUNDS.intersectsWith(mask)) {
            list.add(Multipart.DEFAULT_RENDER_BOUNDS);
        }
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(Multipart.DEFAULT_RENDER_BOUNDS);
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        list.add(Multipart.DEFAULT_RENDER_BOUNDS);
    }
}
