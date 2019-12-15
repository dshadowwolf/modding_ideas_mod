package com.mcmoddev.multiblocktest.util;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;

public interface ICapacitorComponent {
	public MMDBlockWithTile<CapBankControllerTile> getMasterComponent();
	public void setMasterComponent(MMDBlockWithTile<CapBankControllerTile> master);
}
