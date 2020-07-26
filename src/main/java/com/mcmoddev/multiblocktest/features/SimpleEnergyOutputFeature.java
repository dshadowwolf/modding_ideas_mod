package com.mcmoddev.multiblocktest.features;

import javax.annotation.Nullable;

import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.feature.FeatureDirtyLevel;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankInputJackTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankOutputJackTile;
import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;
import com.mcmoddev.multiblocktest.util.SharedStrings;

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
	public static final int DEFAULT_CAPACITY  = MultiBlockTestConfig.config_values.get(SharedStrings.CAPACITY).get(SharedStrings.BANK);
	public static final int DEFAULT_OUT_RATE  = MultiBlockTestConfig.config_values.get(SharedStrings.TRANSMIT).get(SharedStrings.BANK);
	
	public SimpleEnergyOutputFeature(String key, CapBankControllerTile central, TileEntity source) {
		super(key, 0, DEFAULT_CAPACITY, 0, DEFAULT_OUT_RATE);
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
			if (sourceT instanceof CapBankOutputJackTile) {
				CapBankControllerTile zz = ((CapBankOutputJackTile)sourceT).getMasterComponent(); 
				if (zz == null) return;
				else setCoreComponent(zz);
				setDirty(FeatureDirtyLevel.LOAD);
			}
		}
		
		for( EnumFacing facing : EnumFacing.values() ) {
			TileEntity target = getAdjacentTE(facing);
			if(target != null && target.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
				IEnergyStorage es = target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				if (buffer.canTake() && es != null) {
					int maxValue = buffer.getOutputRate();
					int canTake = buffer.take(maxValue, false);
					int storeThis = canTake > maxValue?maxValue:canTake;
					int canExtract = es.extractEnergy(storeThis, true);
					int ready = canExtract < storeThis ? canExtract : storeThis;
					int zaa = target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).receiveEnergy(ready, false);
					buffer.store(zaa, true);
					setDirty(FeatureDirtyLevel.TICK);
				}
			}
		}

		if (core != null) {
			ForgeEnergyStorage controller = core.getStorage();
			int rate = core.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP).extractEnergy(100000000, true);
			buffer.setInputRate(rate);
			if (buffer.canStore() && controller.canTake() && buffer.getStored() > 0) {
				IEnergyStorage e = core.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
				if(e == null) {
					buffer.setInputRate(0);
					return;
				}
				int transferAmount = buffer.getStored() < rate ? buffer.getStored() : rate;
				int toTransferBase = e.extractEnergy(transferAmount, true);
				int toTransferFinal = buffer.store(toTransferBase, false);
				buffer.take(toTransferFinal, true);
				e.extractEnergy(toTransferFinal, false);
			}
			setDirty(FeatureDirtyLevel.TICK);
			buffer.setInputRate(0);
		}
	}

}
