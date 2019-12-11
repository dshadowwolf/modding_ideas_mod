package com.mcmoddev.multiblocktest.util;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IMultiBlockStructure {
	public boolean detectMultiblock();
	public Pair<BlockPos, BlockPos> findLimits();
	public List<Pair<BlockPos, IBlockState>> getContents();
	public List<TileEntity> getTiles();
	public boolean form();
}
