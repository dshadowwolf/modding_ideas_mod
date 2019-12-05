package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.multiblocktest.MultiBlockTest;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CapacitorBank extends Block implements ITileEntityProvider {
	public CapacitorBank(Material material) {
		super(material);
		setTranslationKey(MultiBlockTest.MODID + ".capacitor_bank");
		setRegistryName("capacitor_bank");
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
