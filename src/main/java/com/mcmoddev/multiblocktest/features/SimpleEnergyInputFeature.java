package com.mcmoddev.multiblocktest.features;

import javax.annotation.Nullable;

import com.mcmoddev.lib.energy.ForgeEnergyStorage;
import com.mcmoddev.lib.feature.ForgeEnergyBatteryFeature;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.tileentity.CapBankInputJackTile;
import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;
import com.mcmoddev.multiblocktest.util.SharedStrings;

import net.minecraft.nbt.NBTTagCompound;
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
	public static final int DEFAULT_CAPACITY = MultiBlockTestConfig.config_values.get(SharedStrings.CAPACITY).get(SharedStrings.BANK);
	public static final int DEFAULT_RECV_RATE = MultiBlockTestConfig.config_values.get(SharedStrings.RECEIVE).get(SharedStrings.BANK);

	public SimpleEnergyInputFeature(String key, CapBankControllerTile central, TileEntity source) {
		super(key, 0, DEFAULT_CAPACITY, DEFAULT_RECV_RATE, 0);
		buffer = getEnergyStorage();
		core = central;
		sourceT = source;
	}

	public void setCoreComponent(CapBankControllerTile central) {
		core = central;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub

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
				if (zz == null) return;
				else setCoreComponent(zz);
			}
		}
		
		for( EnumFacing facing : EnumFacing.values() ) {
			TileEntity target = getAdjacentTE(facing);
			if(target != null && target.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
				if (buffer.canStore()) {
					com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("Sanity Check: capacity %dFE :: Input Rate %d FE/t :: Output Rate: %d FE/t", buffer.getCapacity(), buffer.getInputRate(), buffer.getOutputRate());
					int maxValue = buffer.getInputRate();
					int canStore = buffer.store(maxValue, false);
					int storeThis = canStore > maxValue?maxValue:canStore;
					int canExtract = target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).extractEnergy(storeThis, true);
					int ready = canExtract < storeThis ? canExtract : storeThis;
					buffer.store(target.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).extractEnergy(ready, false), true);
					com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("Tried to input %d (of possible %d) from TE at %s", ready, canExtract, target.getPos());
					com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("storeThis: %d -- canStore: %d -- canExtract: %d", storeThis, canStore, canExtract);
					com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("maxValue: %d -- canStore: %d -- storeThis: %d -- canExtract: %d -- ready: %d", maxValue, canStore, storeThis, canExtract, ready);
				}
			}
		}

		
		if (core != null) {
			ForgeEnergyStorage controller = core.getStorage();
			int rate = controller.getInputRate();
			buffer.setoutputRate(rate);
			if (buffer.canTake() && controller.canStore()) {
				int transferAmount = buffer.getStored() < rate ? buffer.getStored() : rate;
				int toTransferBase = controller.store(transferAmount, false);
				int toTransferFinal = buffer.take(toTransferBase, false);
				com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("Tried to transfer %d to core storage (rate %d - avail %d - amount %d)", toTransferFinal, rate, buffer.getStored(), transferAmount);				
				buffer.take(toTransferFinal, true);
				controller.store(toTransferFinal, true);
			}
			buffer.setoutputRate(0);
		}
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub

	}

}
