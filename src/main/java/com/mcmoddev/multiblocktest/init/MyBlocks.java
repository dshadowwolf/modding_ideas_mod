package com.mcmoddev.multiblocktest.init;

import com.mcmoddev.multiblocktest.blocks.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 *
 */
public final class MyBlocks {

	/**
	 *
	 */
	private MyBlocks() {
		//
	}

	/**
	 *
	 */
    public static final Block CAPACITOR_BANK_BASE     = new CapacitorBankBase(Material.IRON);
    public static final Block CAPACITOR_BANK_ADVANCED = new CapacitorBankAdvanced(Material.IRON);
    public static final Block CAPACITOR_BANK_MASSIVE  = new CapacitorBankMassive(Material.IRON);
    public static final Block CAPACITOR_BANK_FINAL    = new CapacitorBankFinal(Material.IRON);
    
    public static final Block[] capacitors = { CAPACITOR_BANK_BASE,  CAPACITOR_BANK_ADVANCED, CAPACITOR_BANK_MASSIVE, CAPACITOR_BANK_FINAL };
}
