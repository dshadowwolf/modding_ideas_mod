package com.mcmoddev.multiblocktest.util;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiBlockStructure {
	public boolean detectMultiblock(BlockPos base, World worldIn);
	public Pair<BlockPos, BlockPos> findLimits(BlockPos base, World worldIn);
	public List<Pair<BlockPos, IBlockState>> getContents(World worldIn);
}
