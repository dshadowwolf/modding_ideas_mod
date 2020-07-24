package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.container.gui.LabelWidgetGui;
import com.mcmoddev.lib.container.gui.layout.GridLayout;
import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
import com.mcmoddev.lib.tile.MMDStandardTileEntity;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;
import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;
import com.mcmoddev.multiblocktest.util.SharedStrings;

public class CapBankOutputJackTile extends MMDStandardTileEntity implements ICapacitorComponent {
	private CapBankControllerTile mainComponent;
	public static final int DEFAULT_CAPACITY = MultiBlockTestConfig.config_values.get(SharedStrings.CAPACITY).get(SharedStrings.BANK);
	public static final int DEFAULT_TRANS_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.TRANSMIT).get(SharedStrings.BANK);
	private final ForgeEnergyStorage buffer;
	
	public CapBankOutputJackTile() {
		this(DEFAULT_TRANS_RATE);
	}
	
	protected CapBankOutputJackTile(final int rate) {
		buffer = this.addFeature(new ForgeEnergyBatteryFeature("battery", 0, DEFAULT_CAPACITY, 0, rate))
				.getEnergyStorage();
	}
	
	public final ForgeEnergyStorage getStorage() {
		return buffer;
	}
		
	@Override
	protected IWidgetGui getMainContentWidgetGui(GuiContext context) {
		if (mainComponent != null)
			return mainComponent.getMainContentWidgetGui(context);
		else
			return new GridLayout(1,1)
					.addPiece(new LabelWidgetGui("This Is A Placeholder!"), 0, 0, 1, 1);
	}

	@Override
	public CapBankControllerTile getMasterComponent() {
		return mainComponent;
	}

	@Override
	public void setMasterComponent(CapBankControllerTile master) {
		mainComponent = master;
	}

}
