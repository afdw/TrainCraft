package com.traincraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "tc", name = "TrainCraft", version = "0.1")
public class TrainCraftMod {
    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Hello World!");
    }
}
