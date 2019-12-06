package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.FeatureWrapperGui;
import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.container.gui.LabelWidgetGui;
import com.mcmoddev.lib.container.gui.layout.GridLayout;
import com.mcmoddev.lib.container.gui.layout.SinglePieceWrapper;
import com.mcmoddev.lib.tile.MMDStandardTileEntity;
import com.mcmoddev.multiblocktest.features.SimpleCapacitorFeature;
import com.mcmoddev.lib.energy.ForgeEnergyStorage;

public class TileCapacitor extends MMDStandardTileEntity {
	public static final int DEFAULT_CAPACITY = 50000;
	public static final int DEFAULT_RATE = 5000;
	protected final ForgeEnergyStorage buffer;
	
	public TileCapacitor() {
		this(DEFAULT_CAPACITY);
	}
	
	public TileCapacitor(final int capacity) {
		this(capacity, DEFAULT_RATE);
	}
	
	public TileCapacitor(final int capacity, final int iorate) {
		super();
		
		this.buffer = this.addFeature(new SimpleCapacitorFeature("battery", 0, capacity, iorate, iorate, this))
		.getEnergyStorage();
		buffer.setInputRate(iorate);
		buffer.setoutputRate(iorate);

	}
	
    @Override
    protected final IWidgetGui getMainContentWidgetGui(final GuiContext context) {
        return new GridLayout(9, 1)
            .addPiece(new FeatureWrapperGui(context, this, "battery"), 0, 0, 1, 1)
            .addPiece(new SinglePieceWrapper(this.getContentWidgetGui(context)), 1, 0, 8, 1);
    }

	public IWidgetGui getContentWidgetGui(final GuiContext context) {
        return new GridLayout(9, 3)
                .addPiece(new LabelWidgetGui("This Is A Placeholder"), 4, 0, 4, 1)
                .addPiece(new LabelWidgetGui(String.format("Input Rate: %d FE/t", buffer.getInputRate())), 4, 1, 4, 1)
                .addPiece(new LabelWidgetGui(String.format("Output Rate: %d FE/t", buffer.getOutputRate())), 4, 2, 4, 1);
	}
	
}
