package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.lib.container.gui.FeatureWrapperGui;
import com.mcmoddev.lib.container.gui.GuiContext;
import com.mcmoddev.lib.container.gui.IWidgetGui;
import com.mcmoddev.lib.container.gui.LabelWidgetGui;
import com.mcmoddev.lib.container.gui.layout.GridLayout;
import com.mcmoddev.lib.container.gui.layout.SinglePieceWrapper;
import com.mcmoddev.lib.tile.MMDStandardTileEntity;
import com.mcmoddev.multiblocktest.features.SimpleEnergyInputFeature;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class CapBankInputJackTile extends MMDStandardTileEntity implements ICapacitorComponent {
	private CapBankControllerTile mainComponent;
	private BlockPos mainComponentPosition;
	
	public CapBankInputJackTile() {
		this(CapBankControllerTile.DEFAULT_RECV_RATE);
	}
	
	protected CapBankInputJackTile(final int rate) {
		this.addFeature(new SimpleEnergyInputFeature("battery", mainComponent, this));
		this.mainComponentPosition = this.getPos();
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
		com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("Pos p (%s) from %d", p, delta);
		this.mainComponentPosition = p;
		if (this.getWorld() == null) return;
		if (!this.getWorld().isRemote) {
			com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("Server World!");
			return;
		}
		TileEntity te = this.getWorld().getTileEntity(p);
		if (te != null && te instanceof CapBankControllerTile) setMasterComponent((CapBankControllerTile)te);
	}
	
	@Override
	public CapBankControllerTile getMasterComponent() {
		com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("%s.getMasterComponent -> %s", this.getClass().getCanonicalName(), mainComponent);
		if ((mainComponent == null || mainComponentPosition == getPos()) && !mainComponentPosition.equals(new BlockPos(0,0,0))) {
			com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("Trying to get main component fresh - pos: %s - world %s", this.mainComponentPosition, this.getWorld());
			if (this.getWorld() == null) {
				return null;
			}
			CapBankControllerTile t = (CapBankControllerTile)this.getWorld().getTileEntity(this.mainComponentPosition);
			com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("got TE --> %s (%s)", t, this.getWorld().getBlockState(this.mainComponentPosition));
			setMasterComponent(t);
			return t;
		}
		return mainComponent;
	}

	@Override
	public void setMasterComponent(CapBankControllerTile master) {
		if (master == null) return;
		this.mainComponentPosition = master.getPos();
		mainComponent = master;
		com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("setMasterComponent -> %s", master);
		((SimpleEnergyInputFeature)getFeature("battery")).setCoreComponent(master);
	}

}
