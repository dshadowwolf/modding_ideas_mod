package com.mcmoddev.multiblocktest.features;

import java.util.LinkedList;
import java.util.List;

import com.mcmoddev.lib.feature.FeatureDirtyLevel;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;

import net.minecraft.util.EnumFacing;

public class SimpleEnergyStorageFeature extends ForgeEnergyBatteryFeature {
	private List<EnumFacing> activeFacings = new LinkedList<>();
	
	public SimpleEnergyStorageFeature() {
		this("battery");
	}
	
	public SimpleEnergyStorageFeature(String key) {
		this(key, CapBankControllerTile.DEFAULT_CAPACITY);
	}
	
	public SimpleEnergyStorageFeature(String key, int capacity) {
		this(key, 0, capacity);
	}
	
	public SimpleEnergyStorageFeature(String key, int initial, int capacity) {
		this(key, initial, capacity, CapBankControllerTile.DEFAULT_RECV_RATE);
	}
	
	public SimpleEnergyStorageFeature(String key, int initial, int capacity, int rate) {
		this( key, initial, capacity, rate, CapBankControllerTile.DEFAULT_TRANS_RATE);
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
	
	/*
    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    	boolean rv = false;
    	if (activeFacings.contains(facing)) rv = super.hasCapability(capability, facing);
    	return rv;
    }

    @Nullable
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    	T rv = null;
    	if (hasCapability(capability, facing)) rv = super.getCapability(capability, facing);
    	return rv;
    }*/
}
