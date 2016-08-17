package com.tc.pipe;

import com.tc.lib.TCPart;
import com.tc.lib.TCProperties;
import com.tc.lib.Utils;
import mcmultipart.capabilities.ISlottedCapabilityProvider;
import mcmultipart.client.multipart.IFastMSRPart;
import mcmultipart.multipart.INormallyOccludingPart;
import mcmultipart.multipart.ISlottedPart;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.List;

public class PartPipe extends TCPart implements INormallyOccludingPart, ISlottedPart, IFastMSRPart, ICapabilityProvider, ISlottedCapabilityProvider, ITickable {
    public static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(4.0 / 16.0, 4.0 / 16.0, 4.0 / 16.0, 12.0 / 16.0, 12.0 / 16.0, 12.0 / 16.0);
    public static final AxisAlignedBB CONNECTION_AABB = new AxisAlignedBB(2.0 / 16.0, 0.0 / 16.0, 2.0 / 16.0, 14.0 / 16.0, 4.0 / 16.0, 14.0 / 16.0);

    public EnumPipeType pipeType = EnumPipeType.NORMAL;
    public BitSet connections = new BitSet(6);
    public BitSet possibles = new BitSet(6);
    public FluidTank tank = new FluidTank(16 * Fluid.BUCKET_VOLUME);
    public double[] cornerHeights = new double[4];

    public PartPipe() {
        possibles.set(0, 6, true);
    }

    public PartPipe(EnumPipeType pipeType) {
        this();
        this.pipeType = pipeType;
    }

    @Override
    protected void addProperties(List<IProperty<?>> properties) {
        super.addProperties(properties);
        properties.add(TCProperties.PIPE_TYPE);
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
                .withProperty(TCProperties.PIPE_TYPE, pipeType)
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
        buf.writeInt(pipeType.ordinal());
        buf.writeLongArray(connections.toLongArray());
        buf.writeLongArray(possibles.toLongArray());
        buf.writeNBTTagCompoundToBuffer(tank.writeToNBT(new NBTTagCompound()));
        for(double cornerHeight : cornerHeights) {
            buf.writeDouble(cornerHeight);
        }
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        pipeType = EnumPipeType.values()[buf.readInt()];
        connections = BitSet.valueOf(buf.readLongArray(new long[6]));
        possibles = BitSet.valueOf(buf.readLongArray(new long[6]));
        try {
            tank = tank.readFromNBT(buf.readNBTTagCompoundFromBuffer());
        } catch(IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < cornerHeights.length; i++) {
            cornerHeights[i] = buf.readDouble();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("pipe_type", pipeType.ordinal());
        tag.setByteArray("connections", connections.toByteArray());
        tag.setByteArray("possibles", possibles.toByteArray());
        tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        for(EnumPipeCorner pipeCorner : EnumPipeCorner.values()) {
            tag.setDouble("corner_heights_" + pipeCorner.name().toLowerCase(), cornerHeights[pipeCorner.ordinal()]);
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        pipeType = EnumPipeType.values()[tag.getInteger("pipe_type")];
        connections = BitSet.valueOf(tag.getByteArray("connections"));
        possibles = BitSet.valueOf(tag.getByteArray("possibles"));
        tank = tank.readFromNBT(tag.getCompoundTag("tank"));
        for(EnumPipeCorner pipeCorner : EnumPipeCorner.values()) {
            cornerHeights[pipeCorner.ordinal()] = tag.getDouble("corner_heights_" + pipeCorner.name().toLowerCase());
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
        if(heldItem == null || !(heldItem.getItem() instanceof ItemPliers)) {
            return false;
        }
        player.swingArm(hand);
        markDirty();
        sendUpdatePacket();
        if(hit.subHit == -1) {
            possibles.set(hit.sideHit.ordinal());
        } else {
            int i = 0;
            for(EnumFacing facing : EnumFacing.values()) {
                if(!connections.get(facing.ordinal())) {
                    continue;
                }
                if(i == hit.subHit) {
                    possibles.clear(facing.ordinal());
                }
                i++;
            }
        }
        markDirty();
        sendUpdatePacket();
        return true;
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
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

    private PartSlot getPartSlot() {
        return PartSlot.CENTER;
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(getPartSlot());
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return possibles.get(facing.ordinal()) || super.hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if(!possibles.get(facing.ordinal())) {
                return super.getCapability(capability, facing);
            }
            //noinspection unchecked
            return (T) new IFluidHandler() {

                @Override
                public IFluidTankProperties[] getTankProperties() {
                    return tank.getTankProperties();
                }

                @Override
                public int fill(FluidStack resource, boolean doFill) {
                    sendUpdatePacket();
                    return tank.fill(resource, doFill);
                }

                @Nullable
                @Override
                public FluidStack drain(FluidStack resource, boolean doDrain) {
                    sendUpdatePacket();
                    return tank.drain(resource, doDrain);
                }

                @Nullable
                @Override
                public FluidStack drain(int maxDrain, boolean doDrain) {
                    sendUpdatePacket();
                    return tank.drain(maxDrain, doDrain);
                }
            };
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, PartSlot slot, EnumFacing facing) {
        return hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, PartSlot slot, EnumFacing facing) {
        return getCapability(capability, facing);
    }

    @Override
    public void update() {
        if(getWorld().isRemote) {
            return;
        }

        for(EnumFacing facing : EnumFacing.values()) {
            TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(facing));
            if(possibles.get(facing.ordinal()) && tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
                IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                FluidStack otherFluidStack = fluidHandler.drain(Integer.MAX_VALUE, false);
                if(otherFluidStack == null || tank.getFluidAmount() > otherFluidStack.amount) {
                    if(facing != EnumFacing.UP || tank.getFluidAmount() >= tank.getCapacity()) {
                        FluidStack myFluidStack = tank.drain((facing == EnumFacing.DOWN) ? 10000 : 100, false);
                        if(myFluidStack != null) {
                            int count = fluidHandler.fill(myFluidStack, true);
                            if(count > 0) {
                                tank.drain(count, true);
                                sendUpdatePacket();
                            }
                        }
                    }
                }
                if(!connections.get(facing.ordinal())) {
                    connections.set(facing.ordinal());
                    sendUpdatePacket();
                }
            } else if(connections.get(facing.ordinal())) {
                connections.clear(facing.ordinal());
                sendUpdatePacket();
            }
        }
    }
}
