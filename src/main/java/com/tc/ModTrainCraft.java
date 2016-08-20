package com.tc;

import com.tc.conveyor.EnumItemConveyorType;
import com.tc.conveyor.ItemConveyor;
import com.tc.conveyor.PartConveyor;
import com.tc.conveyor.SpecialRendererConveyor;
import com.tc.pipe.*;
import com.tc.pliers.ItemPliers;
import com.tc.test.BlockTest;
import com.tc.test.SpecialRendererTest;
import com.tc.test.TileEntityTest;
import mcmultipart.client.multipart.MultipartRegistryClient;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "tc", name = "TrainCraft", version = "0.1")
public class ModTrainCraft {
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("tc") {
        @Override
        public Item getTabIconItem() {
            return ITEM_PIPE;
        }
    };

    public static Item ITEM_PIPE;
    public static Item ITEM_CONVEYOR;
    public static Item ITEM_PLIERS;
    public static Block TEST;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Hello World!");

        ITEM_PIPE = new ItemPipe();
        GameRegistry.register(ITEM_PIPE.setRegistryName("pipe"));
        MultipartRegistry.registerPart(PartPipe.class, "tc:pipe");
        for(EnumPipeType pipeType : EnumPipeType.values()) {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ITEM_PIPE, pipeType.ordinal(), new ModelResourceLocation("tc:pipe/" + pipeType.getName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(ITEM_PIPE, pipeType.ordinal(), new ModelResourceLocation("tc:pipe/" + pipeType.getName(), "inventory"));
        }
        MultipartRegistryClient.bindMultipartSpecialRenderer(PartPipe.class, new SpecialRendererPipe());

        ITEM_CONVEYOR = new ItemConveyor();
        GameRegistry.register(ITEM_CONVEYOR.setRegistryName("conveyor"));
        MultipartRegistry.registerPart(PartConveyor.class, "tc:conveyor");
        for(EnumItemConveyorType itemConveyorType : EnumItemConveyorType.values()) {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ITEM_CONVEYOR, itemConveyorType.ordinal(), new ModelResourceLocation("tc:conveyor", "inventory"));
            ModelLoader.setCustomModelResourceLocation(ITEM_CONVEYOR, itemConveyorType.ordinal(), new ModelResourceLocation("tc:conveyor", "inventory"));
        }
//        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ITEM_CONVEYOR, 0, new ModelResourceLocation("tc:conveyor", "inventory"));
        MultipartRegistryClient.bindMultipartSpecialRenderer(PartConveyor.class, new SpecialRendererConveyor());

        ITEM_PLIERS = new ItemPliers();
        GameRegistry.register(ITEM_PLIERS.setRegistryName("pliers"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ITEM_PLIERS, 0, new ModelResourceLocation("tc:pliers", "inventory"));

        TEST = new BlockTest();
        GameRegistry.register(TEST.setRegistryName("test"));
        GameRegistry.register(new ItemBlock(TEST).setRegistryName("test"));
        GameRegistry.registerTileEntity(TileEntityTest.class, "tc:test");
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTest.class, new SpecialRendererTest());
    }
}
