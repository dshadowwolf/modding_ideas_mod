package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankInputJackTile;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.block.material.Material;

public class CapBankInputJack extends MMDBlockWithTile<CapBankInputJackTile> implements ICapacitorComponent {
	private MMDBlockWithTile<CapBankControllerTile> masterComponent;
	
	public CapBankInputJack(Material material) {
		super(CapBankInputJackTile.class, CapBankInputJackTile::new, material);
	}

	@Override
	public MMDBlockWithTile<CapBankControllerTile> getMasterComponent() {
		return masterComponent;
	}

	@Override
	public void setMasterComponent(MMDBlockWithTile<CapBankControllerTile> master) {
		masterComponent = master;
	}
}
