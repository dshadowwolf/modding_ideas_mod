package com.mcmoddev.multiblocktest.tileentity;

import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;
import com.mcmoddev.multiblocktest.util.SharedStrings;

public class TileCapacitorMassive extends TileCapacitorBase {
	public static final int CAPACITY = MultiBlockTestConfig.config_values.get(SharedStrings.CAPACITY).get(SharedStrings.MASSIVE);
	public static final int RECV_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.RECEIVE).get(SharedStrings.MASSIVE);
	public static final int TRANS_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.TRANSMIT).get(SharedStrings.MASSIVE);
	public TileCapacitorMassive() {
		super(CAPACITY, RECV_RATE, TRANS_RATE);
	}
}
