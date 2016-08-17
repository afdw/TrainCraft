package com.tc.test;

import net.minecraft.tileentity.TileEntity;

public class TileEntityTest extends TileEntity {
    @Override
    public boolean hasFastRenderer() {
        return true;
    }
}
