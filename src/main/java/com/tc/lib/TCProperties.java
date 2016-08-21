package com.tc.lib;

import com.tc.conveyor.EnumConveyorType;
import com.tc.pipe.EnumPipeType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;

public class TCProperties {
    public static final PropertyEnum<EnumPipeType> PIPE_TYPE = PropertyEnum.create("pipe_type", EnumPipeType.class);
    public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("connected_down");
    public static final PropertyBool CONNECTED_UP = PropertyBool.create("connected_up");
    public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("connected_north");
    public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("connected_south");
    public static final PropertyBool CONNECTED_WEST = PropertyBool.create("connected_west");
    public static final PropertyBool CONNECTED_EAST = PropertyBool.create("connected_east");
    public static final PropertyBool CLOSED_DOWN = PropertyBool.create("closed_down");
    public static final PropertyBool CLOSED_UP = PropertyBool.create("closed_up");
    public static final PropertyBool CLOSED_NORTH = PropertyBool.create("closed_north");
    public static final PropertyBool CLOSED_SOUTH = PropertyBool.create("closed_south");
    public static final PropertyBool CLOSED_WEST = PropertyBool.create("closed_west");
    public static final PropertyBool CLOSED_EAST = PropertyBool.create("closed_east");
    public static final PropertyBool CLOSED_NORTH_WEST = PropertyBool.create("closed_north_west");
    public static final PropertyBool CLOSED_SOUTH_WEST = PropertyBool.create("closed_south_west");
    public static final PropertyBool CLOSED_NORTH_EAST = PropertyBool.create("closed_north_east");
    public static final PropertyBool CLOSED_SOUTH_EAST = PropertyBool.create("closed_south_east");
    public static final PropertyBool IS_ELEVATOR = PropertyBool.create("is_elevator");
    public static final PropertyDirection DIRECTION = PropertyDirection.create("direction");
    public static final PropertyEnum<EnumConveyorType> CONVEYOR_TYPE = PropertyEnum.create("conveyor_type", EnumConveyorType.class);
}
