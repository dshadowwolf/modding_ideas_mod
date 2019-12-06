package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.tileentity.TileCapacitorAdvanced;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class CapacitorBankAdvanced extends MMDBlockWithTile<TileCapacitorAdvanced> {
	public CapacitorBankAdvanced(Material material) {
		super(TileCapacitorAdvanced.class, TileCapacitorAdvanced::new, material);
		setTranslationKey(MultiBlockTest.MODID + "." + "advanced_capacitor_bank");
		setRegistryName(new ResourceLocation(MultiBlockTest.MODID, "advanced_capacitor_bank"));
	}

}
