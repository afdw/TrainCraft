package com.tc;

import com.tc.pipe.ItemPipe;
import com.tc.pipe.PartPipe;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
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

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Hello World!");

        ITEM_PIPE = new ItemPipe();
        GameRegistry.register(ITEM_PIPE.setRegistryName("pipe"));
        MultipartRegistry.registerPart(PartPipe.class, "tc:pipe");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ITEM_PIPE, 0, new ModelResourceLocation("tc:pipe", "inventory"));
    }
}
