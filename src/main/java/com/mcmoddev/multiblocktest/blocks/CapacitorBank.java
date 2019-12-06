package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.tileentity.TileCapacitor;

import net.minecraft.block.material.Material;

public class CapacitorBank extends MMDBlockWithTile<TileCapacitor> {
	public CapacitorBank(Material material) {
		super(TileCapacitor.class, TileCapacitor::new, material);
		setTranslationKey(MultiBlockTest.MODID + ".capacitor_bank");
		setRegistryName("capacitor_bank");
	}
}
