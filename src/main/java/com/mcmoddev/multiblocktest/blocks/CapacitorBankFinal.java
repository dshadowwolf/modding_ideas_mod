package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.tileentity.TileCapacitorFinal;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class CapacitorBankFinal extends MMDBlockWithTile<TileCapacitorFinal> {
	public CapacitorBankFinal(Material material) {
		super(TileCapacitorFinal.class, TileCapacitorFinal::new, material);
		setTranslationKey(MultiBlockTest.MODID + "." + "final_capacitor_bank");
		setRegistryName(new ResourceLocation(MultiBlockTest.MODID, "final_capacitor_bank"));
	}
}
