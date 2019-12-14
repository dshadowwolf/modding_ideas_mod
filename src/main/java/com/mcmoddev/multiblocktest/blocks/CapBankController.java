package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;

import net.minecraft.block.material.Material;

public class CapBankController extends MMDBlockWithTile<CapBankControllerTile> {
	public CapBankController(Material material) {
		super(CapBankControllerTile.class, CapBankControllerTile::new, material);
	}
}
