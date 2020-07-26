package com.mcmoddev.multiblocktest.features;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.lib.feature.FeatureDirtyLevel;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;
import com.mcmoddev.multiblocktest.util.SharedStrings;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class SimpleEnergyStorageFeature extends ForgeEnergyBatteryFeature {
	private List<EnumFacing> activeFacings = new LinkedList<>();
	
	public SimpleEnergyStorageFeature() {
		this("battery");
	}
	
	public SimpleEnergyStorageFeature(String key) {
		this(key, MultiBlockTestConfig.config_values.get(SharedStrings.CAPACITY).get(SharedStrings.BANK));
	}
	
	public SimpleEnergyStorageFeature(String key, int capacity) {
		this(key, 0, capacity);
	}
	
	public SimpleEnergyStorageFeature(String key, int initial, int capacity) {
		this(key, initial, capacity, MultiBlockTestConfig.config_values.get(SharedStrings.RECEIVE).get(SharedStrings.BANK));
	}
	
	public SimpleEnergyStorageFeature(String key, int initial, int capacity, int rate) {
		this( key, initial, capacity, rate, MultiBlockTestConfig.config_values.get(SharedStrings.TRANSMIT).get(SharedStrings.BANK));
	}
	
	public SimpleEnergyStorageFeature(String key, int initial, int capacity, int inputRate, int outputRate) {
		super(key, initial, capacity, inputRate, outputRate);
	}

	public void addActiveFacings(EnumFacing... facings) {
		for(EnumFacing f: facings) activeFacings.add(f);
	}
	
	public void dirty() {
		super.setDirty(FeatureDirtyLevel.TICK);
	}
	
    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    	boolean rv = false;
    	if (activeFacings.contains(facing)) rv = super.hasCapability(capability, facing);
    	com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("%s.hasCapability(%s, %s) -> %s", this.getClass().getCanonicalName(), capability, facing, rv);    	
    	return rv;
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    	T rv = null;
    	if (hasCapability(capability, facing)) rv = super.getCapability(capability, facing);
    	com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("%s.getCapability(%s, %s) -> %s", this.getClass().getCanonicalName(), capability, facing, rv);    	
    	return rv;
    }
}
