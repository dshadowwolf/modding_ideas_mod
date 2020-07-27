package com.mcmoddev.multiblocktest.features;

import javax.annotation.Nullable;

import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.feature.FeatureDirtyLevel;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankInputJackTile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class SimpleEnergyInputFeature extends ForgeEnergyBatteryFeature implements ITickable {
	private ForgeEnergyStorage buffer;
	private CapBankControllerTile core;
	private TileEntity sourceT;

	public SimpleEnergyInputFeature(String key, CapBankControllerTile central, TileEntity source) {
		super(key, 0, CapBankControllerTile.DEFAULT_CAPACITY, CapBankControllerTile.DEFAULT_RECV_RATE, 0);
		buffer = getEnergyStorage();
		core = central;
		sourceT = source;
	}

	public void setCoreComponent(CapBankControllerTile central) {
		core = central;
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
			if (sourceT instanceof CapBankInputJackTile) {
				CapBankControllerTile zz = ((CapBankInputJackTile)sourceT).getMasterComponent(); 
				if (zz == null) {
					com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("controller block null ?");
					return;
				}
				else setCoreComponent(zz);
				setDirty(FeatureDirtyLevel.LOAD);
			}
		}
		
		for( EnumFacing facing : EnumFacing.values() ) {
			TileEntity target = getAdjacentTE(facing);
			if(target != null && target.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
				IEnergyStorage es = target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				if (buffer.canStore() && es != null) {
					int maxValue = buffer.getInputRate();
					int canStore = buffer.store(maxValue, false);
					int storeThis = canStore > maxValue?maxValue:canStore;
					int canExtract = es.extractEnergy(storeThis, true);
					int ready = canExtract < storeThis ? canExtract : storeThis;
					int zaa = target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).extractEnergy(ready, false);
					buffer.store(zaa, true);
					setDirty(FeatureDirtyLevel.TICK);
				}
			}
		}

		if (core != null) {
			ForgeEnergyStorage controller = core.getStorage();
			int rate = core.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP).receiveEnergy(100000000, true);
			buffer.setoutputRate(rate);
			if (buffer.canTake() && controller.canStore() && buffer.getStored() > 0) {
				IEnergyStorage e = core.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
				if(e == null) {
					buffer.setoutputRate(0);
					return;
				}
				int transferAmount = buffer.getStored() < rate ? buffer.getStored() : rate;
				int toTransferBase = e.receiveEnergy(transferAmount, true);
				int toTransferFinal = buffer.take(toTransferBase, false);
				buffer.take(toTransferFinal, true);
				e.receiveEnergy(toTransferFinal, false);
			}
			setDirty(FeatureDirtyLevel.TICK);
			buffer.setoutputRate(0);
		}
	}

}
