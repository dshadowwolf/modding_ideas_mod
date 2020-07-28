package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.FeatureWrapperGui;
import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.container.gui.LabelWidgetGui;
import com.mcmoddev.lib.container.gui.layout.GridLayout;
import com.mcmoddev.lib.container.gui.layout.SinglePieceWrapper;
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
	private BlockPos mainComponentPosition;
	
	public CapBankOutputJackTile() {
		this(CapBankControllerTile.DEFAULT_TRANS_RATE);
	}
	
	protected CapBankOutputJackTile(final int rate) {
		buffer = this.addFeature(new SimpleEnergyOutputFeature("battery", mainComponent, this)).getEnergyStorage();
		mainComponentPosition = getPos();
	}
	
	public final ForgeEnergyStorage getStorage() {
		return buffer;
	}
		
	@Override
	protected IWidgetGui getMainContentWidgetGui(GuiContext context) {
		if (mainComponent != null) {
			return new GridLayout(9, 1)
            .addPiece(new FeatureWrapperGui(context, this, "battery"), 0, 0, 1, 1)
            .addPiece(new SinglePieceWrapper(mainComponent.getMainContentWidgetGui(context)), 1, 0, 8, 1);
		} else
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
		this.mainComponentPosition = p;
		if (this.getWorld() == null) return;
		if (!this.getWorld().isRemote) {
			return;
		}

		TileEntity te = this.getWorld().getTileEntity(p);
		if (te != null && te instanceof CapBankControllerTile) setMasterComponent((CapBankControllerTile)te);
	}
	
	@Override
	public CapBankControllerTile getMasterComponent() {
		if ((mainComponent == null || mainComponentPosition == getPos()) && !mainComponentPosition.equals(new BlockPos(0,0,0))) {
			if (this.getWorld() == null) {
				return null;
			}
			CapBankControllerTile t = (CapBankControllerTile)this.getWorld().getTileEntity(this.mainComponentPosition);
			setMasterComponent(t);
			return t;
		}
		return mainComponent;
	}

	@Override
	public void setMasterComponent(CapBankControllerTile master) {
		if (master == null) return;
		if (!master.getPos().equals(new BlockPos(0,0,0))) {
			mainComponentPosition = master.getPos();
		}
		mainComponent = master;
		((SimpleEnergyOutputFeature)getFeature("battery")).setCoreComponent(master);
	}

}
