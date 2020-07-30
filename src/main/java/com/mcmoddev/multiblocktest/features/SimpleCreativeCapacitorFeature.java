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

public class SimpleCreativeCapacitorFeature extends ForgeEnergyBatteryFeature implements ITickable {	
	private final ForgeEnergyStorage buffer;
	private final TileEntity source;
	
	public SimpleCreativeCapacitorFeature(final String key,
			final TileEntity source) {
		super(key, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		this.buffer = getEnergyStorage();
		this.source = source;
	}

	private void doInteraction(TileEntity targetEntity, IEnergyStorage target) {
		//int maxValue = this.buffer.getOutputRate();
		//int canSend = this.buffer.take(maxValue, false);
		int sendMax = target.receiveEnergy(Integer.MAX_VALUE, true);
		if (target.canReceive()) {
			target.receiveEnergy(this.buffer.take(sendMax, true), false);
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
				IEnergyStorage tCap = (IEnergyStorage) target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				doInteraction(target, tCap);
				int maxTake = tCap.extractEnergy(Integer.MAX_VALUE, true);
				tCap.extractEnergy(maxTake, false);
			}
		}
	}
}
