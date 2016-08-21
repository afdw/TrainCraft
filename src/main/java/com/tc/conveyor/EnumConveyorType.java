package com.tc.conveyor;

import net.minecraft.util.IStringSerializable;

public enum EnumConveyorType implements IStringSerializable {
    STRAIGHT,
    LEFT,
    RIGHT,
    UP,
    DOWN;

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
