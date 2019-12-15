package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankOutputJackTile;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.block.material.Material;

public class CapBankOutputJack extends MMDBlockWithTile<CapBankOutputJackTile> implements ICapacitorComponent{
	private MMDBlockWithTile<CapBankControllerTile> masterComponent;
	
	public CapBankOutputJack(Material material) {
		super(CapBankOutputJackTile.class, CapBankOutputJackTile::new, material);
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
