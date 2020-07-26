package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.container.gui.LabelWidgetGui;
import com.mcmoddev.lib.container.gui.layout.GridLayout;
import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.tile.MMDStandardTileEntity;
import com.mcmoddev.multiblocktest.features.SimpleEnergyOutputFeature;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class CapBankOutputJackTile extends MMDStandardTileEntity implements ICapacitorComponent {
	private CapBankControllerTile mainComponent;
	private final ForgeEnergyStorage buffer;
	
	public CapBankOutputJackTile() {
		this(CapBankControllerTile.DEFAULT_TRANS_RATE);
	}
	
	protected CapBankOutputJackTile(final int rate) {
		buffer = this.addFeature(new SimpleEnergyOutputFeature("battery", mainComponent, this)).getEnergyStorage();
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
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound temp = super.writeToNBT(nbt);
		if (mainComponent != null)
			temp.setLong("mb_controller_cap", mainComponent.getPos().toLong());
		return temp;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (!compound.hasKey("mb_controller_cap")) return;
		long delta = compound.getLong("mb_controller_cap");
		BlockPos p = BlockPos.fromLong(delta);
		TileEntity te = world.getTileEntity(p);
		if (te != null && te instanceof CapBankControllerTile) setMasterComponent((CapBankControllerTile)te);
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
