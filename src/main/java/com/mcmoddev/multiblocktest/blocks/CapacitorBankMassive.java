package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.tileentity.TileCapacitorMassive;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class CapacitorBankMassive extends MMDBlockWithTile<TileCapacitorMassive> {
	public CapacitorBankMassive(Material material) {
		super(TileCapacitorMassive.class, TileCapacitorMassive::new, material);
		setTranslationKey(MultiBlockTest.MODID + "." + "massive_capacitor_bank");
		setRegistryName(new ResourceLocation(MultiBlockTest.MODID, "massive_capacitor_bank"));
	}

}
