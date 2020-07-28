package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.FeatureWrapperGui;
import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.container.gui.LabelWidgetGui;
import com.mcmoddev.lib.container.gui.layout.GridLayout;
import com.mcmoddev.lib.container.gui.layout.SinglePieceWrapper;
import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
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
	
    protected final ForgeEnergyStorage buffer;

    public CapBankControllerTile() {
    	this(DEFAULT_CAPACITY);
    }
    
    protected CapBankControllerTile(final int capacity) {
    	this(capacity, DEFAULT_RECV_RATE);
    }
    
    protected CapBankControllerTile(final int capacity, final int receiveRate) {
    	this(capacity, receiveRate, DEFAULT_TRANS_RATE);
    }
    
    protected CapBankControllerTile(final int capacity, final int receiveRate, final int sendRate) {
    	super();
    	
        this.buffer = this.addFeature(new ForgeEnergyBatteryFeature("battery", 0, capacity, receiveRate, sendRate)).getEnergyStorage();
    }
    
    public final ForgeEnergyStorage getStorage() {
    	return buffer;
    }

    protected final IWidgetGui getMainContentWidgetGui(final GuiContext context) {
        return new GridLayout(9, 1)
            .addPiece(new FeatureWrapperGui(context, this, "battery"), 0, 0, 1, 1)
            .addPiece(new SinglePieceWrapper(this.getContentWidgetGui(context)), 1, 0, 8, 1);
    }

	public IWidgetGui getContentWidgetGui(final GuiContext context) {
        return new GridLayout(1, 5)
                .addPiece(new LabelWidgetGui("This Is A Placeholder"), 0, 0, 1, 1)
                .addPiece(new LabelWidgetGui(String.format("Max Input Rate: %d FE/t", buffer.getInputRate())), 0, 1, 1, 1)
                .addPiece(new LabelWidgetGui(String.format("Max Output Rate: %d FE/t", buffer.getOutputRate())), 0, 2, 1, 1)
                .addPiece(new LabelWidgetGui(String.format("Max Storage: %d FE", buffer.getCapacity())), 0, 3, 1, 1)
                .addPiece(new LabelWidgetGui(String.format("Current Storage: %d FE", buffer.getStored())), 0,  4, 1, 1);
	}
}
