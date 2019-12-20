package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.tile.MMDStandardTileEntity;
import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;
import com.mcmoddev.multiblocktest.util.SharedStrings;

public class CapBankControllerTile extends MMDStandardTileEntity {
	public static final int DEFAULT_CAPACITY = MultiBlockTestConfig.config_values.get(SharedStrings.CAPACITY).get(SharedStrings.BANK);
	public static final int DEFAULT_RECV_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.RECEIVE).get(SharedStrings.BANK);
	public static final int DEFAULT_TRANS_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.TRANSMIT).get(SharedStrings.BANK);
	public static final int DEFAULT_CAP_BOOST = MultiBlockTestConfig.config_values.get(SharedStrings.MODIFIERS).get(SharedStrings.BOOST_C);
	public static final int DEFAULT_RS_BOOST = MultiBlockTestConfig.config_values.get(SharedStrings.MODIFIERS).get(SharedStrings.BOOST_RS);
	public static final int DEFAULT_D_BOOST = MultiBlockTestConfig.config_values.get(SharedStrings.MODIFIERS).get(SharedStrings.BOOST_D);
	public static final int DEFAULT_E_BOOST = MultiBlockTestConfig.config_values.get(SharedStrings.MODIFIERS).get(SharedStrings.BOOST_E);
	public static final int DEFAULT_N_BOOST = MultiBlockTestConfig.config_values.get(SharedStrings.MODIFIERS).get(SharedStrings.BOOST_N);
	
	@Override
	protected IWidgetGui getMainContentWidgetGui(GuiContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
