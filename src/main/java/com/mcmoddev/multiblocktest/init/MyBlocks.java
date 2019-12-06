package com.mcmoddev.multiblocktest.init;

import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.blocks.CapacitorBank;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 */
@GameRegistry.ObjectHolder(MultiBlockTest.MODID)
public final class MyBlocks {

	/**
	 *
	 */
	private MyBlocks() {
		//
	}

	/**
	 *
	 */
    public static final Block CAPACITOR_BANK = new CapacitorBank(Material.IRON);
}
