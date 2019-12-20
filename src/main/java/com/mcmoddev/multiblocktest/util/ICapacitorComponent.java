package com.mcmoddev.multiblocktest.util;

import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;

public interface ICapacitorComponent {
	CapBankControllerTile getMasterComponent();
	public void setMasterComponent(CapBankControllerTile master);
}
