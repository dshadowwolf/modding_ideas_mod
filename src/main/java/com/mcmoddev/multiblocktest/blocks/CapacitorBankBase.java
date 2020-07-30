package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.tileentity.TileCapacitorBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class CapacitorBankBase extends MMDBlockWithTile<TileCapacitorBase> {
	public CapacitorBankBase(Material material) {
		super(TileCapacitorBase.class, () -> new TileCapacitorBase(), material);
		setTranslationKey(MultiBlockTest.MODID + "." + "basic_capacitor_bank");
		setRegistryName(new ResourceLocation(MultiBlockTest.MODID, "capacitor_bank"));
	}
	
	@Override
	public boolean isFullCube(IBlockState blockStateIn) {
		return false; 
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState blockStateIn) {
		return false;
	}
}
