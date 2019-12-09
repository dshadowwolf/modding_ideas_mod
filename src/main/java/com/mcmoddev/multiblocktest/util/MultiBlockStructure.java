package com.mcmoddev.multiblocktest.util;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.lang3.tuple.MutableTriple;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MultiBlockStructure implements IMultiBlockStructure {

	private final int xWidth;
	private final int zWidth;
	private final int height;
	private final List<IBlockState> validBlocks = new LinkedList<>();
	private final MutableTriple<Integer,Integer,Integer> detected = MutableTriple.of(0, 0, 0);
	private BlockPos minPos = new BlockPos(0,0,0);
	private BlockPos maxPos = new BlockPos(0,0,0);
	
	public MultiBlockStructure(int xWidth, int zWidth, int height, List<IBlockState> validBlocks) {
		this.xWidth = xWidth;
		this.zWidth = zWidth;
		this.height = height;
		this.validBlocks.addAll(validBlocks);
	}

	/**
	 * Get the maximum possible size of this Multiblock
	 * @return 3-Tuple (Triple) of xMax, zMax, height (left/middle/right)
	 */
	public Triple<Integer,Integer,Integer> maxSize() {
		return Triple.of(xWidth,zWidth,height);
	}
	
	/**
	 * Get the detected size of this Multiblock
	 * @return 3-Tuple (Triple) of x, z, height (left/middle/right)
	 */
	public Triple<Integer,Integer,Integer> detectedSize() {
		return Triple.of(detected.left, detected.middle, detected.right);
	}
	
	/**
	 * Is the controller placed as part of a valid multiblock ?
	 * @param origin The multiblock controller
	 * @return Is the multiblock valid ?
	 */
	abstract boolean isValidMultiblock(IBlockState origin);

	/**
	 * Assuming a cuboid multiblock this will first seek out the actual extents of the possible structure,
	 * then walk the blocks in that region. If it finds only valid blocks, it will return true, otherwise it will
	 * return false.
	 * 
	 * @param BlockPos base - The location of the controller block
	 * @param World worldIn - The world where the multiblock is
	 * 
	 * @return whether the multiblock has been detected or not
	 */
	@Override
	public boolean detectMultiblock(BlockPos base, World worldIn) {
		Pair<BlockPos, BlockPos> ext = findLimits(base, worldIn);
		List<IBlockState> blocks = new LinkedList<>();
		
		BlockPos.getAllInBox(ext.getLeft(), ext.getRight()).forEach( pos -> blocks.add(worldIn.getBlockState(pos)) );
	
		return validBlocks.containsAll(blocks);
	}

	/**
	 * Assuming we have a valid position for the controlling block, find the maximum and minimum positions of blocks
	 * in the block list that are within the maximum distance specified from each other.
	 * 
	 * @param BlockPos base - The location of the controller block
	 * @param World worldIn - The world where the multiblock is
	 * 
	 * @return Pair<BlockPos, BlockPos> - The minimum (left value) and maximum (right value) extents
	 */
	@Override
	public Pair<BlockPos, BlockPos> findLimits(BlockPos base, World worldIn) {
        final Queue<BlockPos> remainingCandidates = new ArrayDeque<>();
        final Set<BlockPos> visited = new HashSet<>();
        BlockPos.MutableBlockPos min = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos max = new BlockPos.MutableBlockPos();

        remainingCandidates.add(base);
        while(!remainingCandidates.isEmpty())
        {
            BlockPos next = remainingCandidates.remove();
            visited.add(next);
            if (next == base || validBlocks.contains(worldIn.getBlockState(next)))
            {
                min.setPos(
                        Math.min(min.getX(),next.getX()),
                        Math.min(min.getY(),next.getY()),
                        Math.min(min.getZ(),next.getZ())
                );
                max.setPos(
                        Math.max(max.getX(),next.getX()),
                        Math.max(max.getY(),next.getY()),
                        Math.max(max.getZ(),next.getZ())
                );
                for(EnumFacing f : EnumFacing.VALUES)
                {
                    BlockPos newCandidate = next.offset(f);
                    if (!visited.contains(newCandidate) &&
                            Math.abs(newCandidate.getX()-base.getX()) <= xWidth &&
                            Math.abs(newCandidate.getY()-base.getY()) <= height &&
                            Math.abs(newCandidate.getZ()-base.getZ()) <= zWidth
                        )
                    {
                        remainingCandidates.add(newCandidate);
                    }
                }
            }
        }
        
        detected.left = Math.abs(max.getX() - min.getX());
        detected.middle = Math.abs(max.getY() - min.getY());
        detected.right = Math.abs(max.getZ() - min.getZ());
        
        minPos = min;
        maxPos = max;
        return Pair.of(min,max);
    }
}
