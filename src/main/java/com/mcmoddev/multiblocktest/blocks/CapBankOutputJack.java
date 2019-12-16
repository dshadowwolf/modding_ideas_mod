package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankOutputJackTile;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class CapBankOutputJack extends MMDBlockWithTile<CapBankOutputJackTile> implements ICapacitorComponent{
	private MMDBlockWithTile<CapBankControllerTile> masterComponent;
	
	public CapBankOutputJack(Material material) {
		super(CapBankOutputJackTile.class, CapBankOutputJackTile::new, material);
		setTranslationKey(MultiBlockTest.MODID + "." + "capacitor_bank_output");
		setRegistryName(new ResourceLocation(MultiBlockTest.MODID, "capacitor_bank_output"));
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
