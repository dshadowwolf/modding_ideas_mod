package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankInputJackTile;

import net.minecraft.block.material.Material;

public class CapBankInputJack extends MMDBlockWithTile<CapBankInputJackTile> {
	public CapBankInputJack(Material material) {
		super(CapBankInputJackTile.class, CapBankInputJackTile::new, material);
	}
}
