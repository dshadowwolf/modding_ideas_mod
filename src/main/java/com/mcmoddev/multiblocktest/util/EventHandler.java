package com.mcmoddev.multiblocktest.util;

import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.events.MMDLibRegisterBlockTypes;
import com.mcmoddev.lib.events.MMDLibRegisterBlocks;
import com.mcmoddev.lib.events.MMDLibRegisterItemTypes;
import com.mcmoddev.lib.events.MMDLibRegisterItems;
import com.mcmoddev.lib.block.MMDBlockWithTile;

import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.init.MyBlocks;

@EventBusSubscriber(modid = MultiBlockTest.MODID)
public class EventHandler {
	private EventHandler() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}
	
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
    	event.getRegistry().registerAll(MyBlocks.cap_items);
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    	event.getRegistry().registerAll(MyBlocks.capacitors);
    	for( Block cap: MyBlocks.capacitors) {
    		((MMDBlockWithTile)cap).registerTile();
    	}
    }

    @SubscribeEvent
    public static void registerBlockTypes(final MMDLibRegisterBlockTypes event) {
    	
    }
    
    @SubscribeEvent
    public static void registerMMDLibBlocks(final MMDLibRegisterBlocks event) {
    	for( Block cap: MyBlocks.capacitors) {
        	com.mcmoddev.lib.init.Materials.DEFAULT.addNewBlock(cap.getRegistryName().getPath(), cap);
    	}
    }

    @SubscribeEvent
    public static void registerItemTypes(final MMDLibRegisterItemTypes event) {
    	
    }
    
    @SubscribeEvent
    public static void registerMMDLibItems(final MMDLibRegisterItems event) {
    	
    }
    
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		OBJLoader.INSTANCE.addDomain(MultiBlockTest.MODID);
		for(Item item: MyBlocks.cap_items) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}    
}
