package com.mcmoddev.multiblocktest.features;

import javax.annotation.Nullable;

import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.feature.FeatureDirtyLevel;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankOutputJackTile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class SimpleEnergyOutputFeature extends ForgeEnergyBatteryFeature implements ITickable {
	private ForgeEnergyStorage buffer;
	private CapBankControllerTile core;
	private TileEntity sourceT;

	public SimpleEnergyOutputFeature(String key, CapBankControllerTile central, TileEntity source) {
		super(key, 0, CapBankControllerTile.DEFAULT_CAPACITY, 0, CapBankControllerTile.DEFAULT_TRANS_RATE);
		this.buffer = getEnergyStorage();
		this.core = central;
		this.sourceT = source;
	}
	
	public void setCoreComponent(CapBankControllerTile central) {
		this.core = central;
	}

	@Nullable
	private TileEntity getAdjacentTE(EnumFacing facing) {
		BlockPos pos = sourceT.getPos().offset(facing);
		World world = sourceT.getWorld();
		
		if (world == null || !world.isBlockLoaded(pos)) return null;
		return world.getTileEntity(pos);
	}

	@Override
	public void update() {
		if (core == null ) {
			if (sourceT instanceof CapBankOutputJackTile) {
				CapBankControllerTile zz = ((CapBankOutputJackTile)sourceT).getMasterComponent(); 
				if (zz == null) {
					com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("controller block null ?");
					return;
				}
				else setCoreComponent(zz);
				setDirty(FeatureDirtyLevel.LOAD);
			}
		}
		
		for(EnumFacing facing : EnumFacing.VALUES) {
			TileEntity te = getAdjacentTE(facing);
			if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
				IEnergyStorage es = te.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				if (es.canReceive() && buffer.canTake()) {
					int canSend = buffer.getStored() < buffer.getOutputRate() ? buffer.getStored() : buffer.getOutputRate();
					int actualSend = es.receiveEnergy(canSend, true);
					buffer.take(es.receiveEnergy(actualSend, false), true);
				}
			}
		}
		
		buffer.setInputRate(core.getStorage().getOutputRate());
		if (core.getStorage().canTake()) {
			int canTakeMax = buffer.getStored() < buffer.getCapacity() ? buffer.getInputRate() : 0;
			if (canTakeMax == 0) return;
			int canTakeActual = buffer.store(canTakeMax, false);
			IEnergyStorage coreES = core.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
			if (coreES == null) return;
			int willTake = coreES.extractEnergy(canTakeActual, true);
			buffer.store(coreES.extractEnergy(willTake, false), true);
		}
		setDirty(FeatureDirtyLevel.TICK);
		buffer.setInputRate(0);
	}

}
