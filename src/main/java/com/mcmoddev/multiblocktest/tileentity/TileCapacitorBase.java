package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.FeatureWrapperGui;
import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.container.gui.LabelWidgetGui;
import com.mcmoddev.lib.container.gui.layout.GridLayout;
import com.mcmoddev.lib.container.gui.layout.SinglePieceWrapper;
import com.mcmoddev.lib.tile.MMDStandardTileEntity;
import com.mcmoddev.multiblocktest.features.SimpleCapacitorFeature;
import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;
import com.mcmoddev.multiblocktest.util.SharedStrings;
import com.mcmoddev.lib.energy.ForgeEnergyStorage;

public class TileCapacitorBase extends MMDStandardTileEntity {
	public static final int DEFAULT_CAPACITY = MultiBlockTestConfig.config_values.get(SharedStrings.CAPACITY).get(SharedStrings.BASE);
	public static final int DEFAULT_RECV_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.RECEIVE).get(SharedStrings.BASE);
	public static final int DEFAULT_TRANS_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.TRANSMIT).get(SharedStrings.BASE);
	protected final ForgeEnergyStorage buffer;
	
	public TileCapacitorBase() {
		this(DEFAULT_CAPACITY);
	}
	
	public TileCapacitorBase(final int capacity) {
		this(capacity, DEFAULT_TRANS_RATE);
	}
	
	public TileCapacitorBase(final int capacity, final int iorate) {
		this(capacity, iorate, iorate);
	}
	
	public TileCapacitorBase(final int capacity, final int inputRate, final int outputRate) {
		super();
		
		this.buffer = this.addFeature(new SimpleCapacitorFeature("battery", 0, capacity, inputRate, outputRate, this))
				.getEnergyStorage();
	}
	
    @Override
    protected final IWidgetGui getMainContentWidgetGui(final GuiContext context) {
        return new GridLayout(9, 1)
            .addPiece(new FeatureWrapperGui(context, this, "battery"), 0, 0, 1, 1)
            .addPiece(new SinglePieceWrapper(this.getContentWidgetGui(context)), 1, 0, 8, 1);
    }

	public IWidgetGui getContentWidgetGui(final GuiContext context) {
        return new GridLayout(1, 3)
                .addPiece(new LabelWidgetGui("This Is A Placeholder"), 0, 0, 1, 1)
                .addPiece(new LabelWidgetGui(String.format("Input Rate: %d FE/t", buffer.getInputRate())), 0, 1, 1, 1)
                .addPiece(new LabelWidgetGui(String.format("Output Rate: %d FE/t", buffer.getOutputRate())), 0, 2, 1, 1);
	}
	
}
