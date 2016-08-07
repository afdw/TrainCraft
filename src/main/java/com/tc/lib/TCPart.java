package com.tc.lib;

import mcmultipart.MCMultiPartMod;
import mcmultipart.multipart.Multipart;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;

import java.util.ArrayList;
import java.util.List;

public class TCPart extends Multipart {
    protected void addProperties(List<IProperty<?>> properties) {
    }

    @Override
    public BlockStateContainer createBlockState() {
        List<IProperty<?>> properties = new ArrayList<>();
        addProperties(properties);
        return new BlockStateContainer(MCMultiPartMod.multipart, properties.toArray(new IProperty[0]));
    }
}
