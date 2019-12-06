package com.mcmoddev.multiblocktest.util;

import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.events.MMDLibRegisterBlockTypes;
import com.mcmoddev.lib.events.MMDLibRegisterBlocks;
import com.mcmoddev.lib.events.MMDLibRegisterItemTypes;
import com.mcmoddev.lib.events.MMDLibRegisterItems;
import com.mcmoddev.lib.block.MMDBlockWithTile;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;

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
    	event.getRegistry().register(new ItemBlock(MyBlocks.CAPACITOR_BANK).setRegistryName(MyBlocks.CAPACITOR_BANK.getRegistryName()));
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    	event.getRegistry().register(MyBlocks.CAPACITOR_BANK);
    	((MMDBlockWithTile)MyBlocks.CAPACITOR_BANK).registerTile();
    }

    @SubscribeEvent
    public static void registerBlockTypes(final MMDLibRegisterBlockTypes event) {
    	
    }
    
    @SubscribeEvent
    public static void registerMMDLibBlocks(final MMDLibRegisterBlocks event) {
    	com.mcmoddev.lib.init.Materials.DEFAULT.addNewBlock("Capacitor Bank", MyBlocks.CAPACITOR_BANK);
    }

    @SubscribeEvent
    public static void registerItemTypes(final MMDLibRegisterItemTypes event) {
    	
    }
    
    @SubscribeEvent
    public static void registerMMDLibItems(final MMDLibRegisterItems event) {
    	
    }
    
}
