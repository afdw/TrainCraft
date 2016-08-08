package com.tc.pipe;

import net.minecraft.util.IStringSerializable;

public enum EnumPipeType implements IStringSerializable {
    NORMAL,
    TRANSPARENT;

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
