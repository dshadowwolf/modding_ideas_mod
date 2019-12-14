package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankOutputJackTile;

import net.minecraft.block.material.Material;

public class CapBankOutputJack extends MMDBlockWithTile<CapBankOutputJackTile> {
	public CapBankOutputJack(Material material) {
		super(CapBankOutputJackTile.class, CapBankOutputJackTile::new, material);
	}
}
