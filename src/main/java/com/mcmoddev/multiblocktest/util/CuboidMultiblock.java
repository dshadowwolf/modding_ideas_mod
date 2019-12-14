package com.mcmoddev.multiblocktest.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CuboidMultiblock extends MultiBlockStructure {

	public CuboidMultiblock(BlockPos origin, World worldIn, int xWidth, int zWidth, int height,
			List<IBlockState> validBlocks) {
		super(origin, worldIn, xWidth, zWidth, height, validBlocks);
	}
	
	/**
	 * Get a list with 6 maps, each containing the blocks of the various walls
	 * @return List with 6 entries containing the walls of the multiblock - top (0)/bottom (1)/min-x wall (2)/max-x wall (3)/min-z wall (4)/max-z wall (5)
	 */
	protected List<Map<BlockPos, IBlockState>> getContainingWalls() {
		List<Map<BlockPos, IBlockState>> data = new Vector<>();
		Map<BlockPos, IBlockState> rawBlocks = getStructureAsMap();
		
		Pair<BlockPos,BlockPos> limits = findLimits();
		// top
		data.add(rawBlocks.entrySet().stream().filter(ent -> ent.getKey().getY() == limits.getRight().getY()).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));
		// bottom
		data.add(rawBlocks.entrySet().stream().filter(ent -> ent.getKey().getY() == limits.getLeft().getY()).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));
		// X min and max
		data.add(rawBlocks.entrySet().stream().filter(ent -> ent.getKey().getX() == limits.getRight().getX()).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));
		data.add(rawBlocks.entrySet().stream().filter(ent -> ent.getKey().getX() == limits.getLeft().getX()).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));
		// Z min and max
		data.add(rawBlocks.entrySet().stream().filter(ent -> ent.getKey().getZ() == limits.getRight().getZ()).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));
		data.add(rawBlocks.entrySet().stream().filter(ent -> ent.getKey().getZ() == limits.getLeft().getZ()).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));

		return Collections.unmodifiableList(data);
	}
	
	/**
	 * getStructure() returns a list of pairs - this converts it to a map
	 * @return Map<BlockPos, IBlockState> containing the same data as getStructure(), just in a different format (this is immutable)
	 */
	protected Map<BlockPos, IBlockState> getStructureAsMap() {
		return Collections.unmodifiableMap(getStructure().stream().collect(Collectors.toMap(p -> p.getLeft(), p -> p.getRight())));
	}
}
