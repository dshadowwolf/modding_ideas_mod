package com.mcmoddev.multiblocktest.features;

import javax.annotation.Nullable;

import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.feature.FeatureDirtyLevel;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class SimpleCapacitorFeature extends ForgeEnergyBatteryFeature implements ITickable {	
	private final ForgeEnergyStorage buffer;
	private final TileEntity source;
	
	public SimpleCapacitorFeature(final String key,
			final int initial,
			final int capacity,
			final int inputRate,
			final int outputRate,
			final TileEntity source) {
		super(key, initial, capacity, inputRate, outputRate);
		this.buffer = getEnergyStorage();
		this.source = source;
	}

	private void doInteraction(TileEntity targetEntity, IEnergyStorage target) {
		int maxValue = this.buffer.getOutputRate();
		int canSend = this.buffer.take(maxValue, false);
		int sendMax = target.receiveEnergy(canSend, true);
		if (target.canReceive() && sendMax > 0 && canSend > 0) {
			target.receiveEnergy(this.buffer.take(sendMax>canSend?canSend:sendMax, true), false);
			this.setDirty(FeatureDirtyLevel.GUI);
		}	
	}
	
	@Nullable
	private TileEntity getAdjacentTE(EnumFacing facing) {
		BlockPos pos = source.getPos().offset(facing);
		World world = source.getWorld();
		
		if (world == null || !world.isBlockLoaded(pos)) return null;
		return world.getTileEntity(pos);
	}
	
	@Override
	public void update() {
		for( EnumFacing facing : EnumFacing.values() ) {
			TileEntity target = getAdjacentTE(facing);
			if(target != null && target.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
				if (buffer.canTake()) {
					doInteraction(target, target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()));
				} else if (buffer.canStore()) {
					int maxValue = this.buffer.getInputRate();
					int canStore = this.buffer.store(maxValue, false);
					this.buffer.store(target.getCapability(CapabilityEnergy.ENERGY, facing).extractEnergy(canStore > maxValue?maxValue:canStore, false), true);
				}
			}
		}
	}
}
