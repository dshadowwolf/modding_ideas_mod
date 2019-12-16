package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankInputJackTile;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class CapBankInputJack extends MMDBlockWithTile<CapBankInputJackTile> implements ICapacitorComponent {
	private MMDBlockWithTile<CapBankControllerTile> masterComponent;
	
	public CapBankInputJack(Material material) {
		super(CapBankInputJackTile.class, CapBankInputJackTile::new, material);
		setTranslationKey(MultiBlockTest.MODID + "." + "capacitor_bank_input");
		setRegistryName(new ResourceLocation(MultiBlockTest.MODID, "capacitor_bank_input"));
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
