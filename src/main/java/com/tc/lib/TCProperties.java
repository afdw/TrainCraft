package com.tc.lib;

import com.tc.pipe.EnumPipeType;
import net.minecraft.block.properties.PropertyBool;
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
}
