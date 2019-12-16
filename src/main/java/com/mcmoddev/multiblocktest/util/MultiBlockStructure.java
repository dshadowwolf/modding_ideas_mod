package com.mcmoddev.multiblocktest.util;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import org.apache.commons.lang3.tuple.MutableTriple;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * Meant as a container to hold the logic for detecting the validity of a multiblock and handling some bits of interaction with the world.
 * The constructor is about as simple as I've been able to make it and I've tried to provide sane defaults for most functions, though the
 * overloads provided for isMultiblockValid() and checkStructure() need to be overridden - I'd have left them abstract, but decided to
 * not hobble implementors with needing to write the extra code there.
 * 
 * @author Daniel Hazelton <dshadowwolf@gmail.com>
 * @since 1-DEC-2019
 * @version 1.0
 * @implNote Version 1.0 of this code is wholly independent of anything except Minecraft and the Apache Commons - future versions will, likely, tie into MMDLib
 */
public abstract class MultiBlockStructure implements IMultiBlockStructure {

	/* these should be a small, fixed value - perhaps even clamped to, say, no more than 128... */
	private final int xWidth; // maximum x size of the structure
	private final int zWidth; // maximum z size of the structure
	private final int height; // maximum height of the structure
	
	private final Set<IBlockState> validBlocks = new HashSet<>(); // list of blocks that make up a valid structure
	
	// detected size of the structure
	private final MutableTriple<Integer,Integer,Integer> detected = MutableTriple.of(0, 0, 0);
	
	/*
	 *  the two values are used to get both just the center contents of the multiblock - if such is important to the multiblock itself -
	 *  and to get the entire multiblock overall, which should be used as part of determining if the structure is valid.
	 */
	private BlockPos minPos = new BlockPos(0,0,0); // maximum position of contiguous valid blocks
	private BlockPos maxPos = new BlockPos(0,0,0); // minimum position of contiguous valid blocks
	
	private BlockPos origin; // position of the TE/Block in the world that has created this instance to attempt to form a multiblock
	private World world; // IWorldAccess more than anything - without this we'd be unable to actually get the blocks or blockstates

	// basic constructor - takes all non-calculable important values as parameters.
	public MultiBlockStructure(BlockPos origin, World worldIn, int xWidth, int zWidth, int height, List<IBlockState> validBlocks) {
		// setup the values we get as parameters...
		this.origin = origin;
		this.world = worldIn;
		this.xWidth = xWidth;
		this.zWidth = zWidth;
		this.height = height;
		this.validBlocks.addAll(validBlocks);
		
		// setup most of the rest
		findLimits();
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
	 * This is the recommended path at this time...
	 * @param origin The multiblock controller
	 * @return Is the multiblock valid ?
	 */
	abstract public boolean isValidMultiblock();
	
	/**
	 * Is the controller placed as part of a valid multiblock ?
	 * @param origin The multiblock controller
	 * @return Is the multiblock valid ?
	 */
	public boolean isValidMultiblock(IBlockState origin) {
		return checkMultiblockStructure(origin);
	}

	/**
	 * Is the controller placed as part of a valid multiblock ?
	 * Uses the more rigid structure style...
	 * @param origin The multiblock controller
	 * @return Is the multiblock valid ?
	 */
	public boolean isValidMultiblock(IBlockState origin, Map<Vec3i, IBlockState> structure) {
		return checkMultiblockStructure(origin, structure);
	}
	
	/**
	 * Check the structure of the multiblock. This should be called from within the isValidMultiblock function
	 * 
	 * @param IBlockState origin - originating block 
	 * @param Map<Vec3i, IBlockState> structure - a Map of what blocks should be in which position of the multiblock
	 * @return Does the multiblock conform to the specified structure ?
	 */
	protected boolean checkMultiblockStructure(IBlockState origin, Map<Vec3i, IBlockState> structure) { return true; }

	/**
	 * Is the controller placed as part of a valid multiblock ?
	 * This is the recommended path...
	 * @return Is the multiblock valid ?
	 */
	abstract protected boolean checkMultiblockStructure();

	/**
	 * Same as checkStructure(Map<>) but allows for a less rigid definition
	 * 
	 * @param IBlockState origin - originating block 
	 * @return Does the multiblock conform to the specified structure ?
	 */
	protected boolean checkMultiblockStructure(IBlockState origin) { return true; }
	
	/**
	 * Find and return a list of all Tile Entities that are part of the multiblock. This will include the
	 * controller that is actually trying to form the multiblock. This is so the system can find the various
	 * input and output parts.
	 * 
	 * @return List<TileEntity> -- a list of all Tile Entities found within the structure
	 */
	@Override
	public List<TileEntity> getTiles() {
		// Okay, this probably looks a bit odd and scary, so lets explain...
		// the outermost code (the if statement and terminal return) is part of an error-check
		// the inner return uses a small feature from the Java Collections system to turn the
		// Iterator<BlockPos> that we start with into a stream, then converts it to a set of
		// either null's or Tile Entities, before filtering out the null's and collecting
		// everything into the List<TileEntity> that is needed for a return to the user.
		if (minPos != null && maxPos != null) {
			return StreamSupport.stream(BlockPos.getAllInBox(minPos, maxPos).spliterator(), false)
					.map(p -> world.getTileEntity(p))
					.filter(p -> p != null).collect(Collectors.toList());
		}
		return Collections.<TileEntity>emptyList();		
	}
	
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
	public boolean detectMultiblock() {
		Pair<BlockPos, BlockPos> ext = findLimits();
		List<IBlockState> blocks = StreamSupport.stream(BlockPos.getAllInBox(ext.getLeft(), ext.getRight()).spliterator(), false)
				.map(bp -> Pair.of(bp, world.getBlockState(bp)))
				.filter(data -> !data.getRight().getBlock().isAir(data.getRight(), world, data.getLeft()))
				.map(data -> data.getRight())
				.collect(Collectors.toList());
		
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
	public Pair<BlockPos, BlockPos> findLimits() {
        final Queue<BlockPos> remainingCandidates = new ArrayDeque<>();
        final Set<BlockPos> visited = new HashSet<>();
        BlockPos.MutableBlockPos min = new BlockPos.MutableBlockPos(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        BlockPos.MutableBlockPos max = new BlockPos.MutableBlockPos(Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);

        remainingCandidates.add(origin);
        while(!remainingCandidates.isEmpty())
        {
            BlockPos next = remainingCandidates.remove();
            visited.add(next);
            if (next == origin || validBlocks.contains(world.getBlockState(next)))
            {
            	//com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.info("%s == %s == %s or validBlocks.contains(%s) == %s", next, origin, next == origin, world.getBlockState(next), validBlocks.contains(world.getBlockState(next)));
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
                            Math.abs(newCandidate.getX()-origin.getX()) <= xWidth &&
                            Math.abs(newCandidate.getY()-origin.getY()) <= height &&
                            Math.abs(newCandidate.getZ()-origin.getZ()) <= zWidth
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

	/**
	 * Called after we're mostly setup (ie: we've detected that we're valid) this returns a list of the blocks
	 * in the interior of the multiblock and their positions.
	 * 
	 * @return List of Pair<BlockPos, IBlockState> describing the blocks in the interior and their contents
	 */
	@Override
	public List<Pair<BlockPos, IBlockState>> getContents() {
		// Okay, this probably looks rather familiar - it is the same basic logic as getTiles()
		// but instead of just mapping the BlockPos values to a TileEntity and filtering out null's
		// this maps things to a 2-Tuple (a Pair) of values, the BlockPos and the IBlockState for the block
		// at that position. The filter is there solely because I'm uncertain of whether BlockPos.getAllInBox()
		// will return a BlockPos for net.minecraft.init.Blocks.AIR or not.
		if (minPos != null && maxPos != null) {
			Vec3i addSub = new Vec3i(1,1,1);
			return StreamSupport.stream(BlockPos.getAllInBox(minPos.add(addSub), maxPos.subtract(addSub)).spliterator(), false)
					.map(p -> Pair.of(p, world.getBlockState(p)))
					.filter(p -> p.getRight() != null && p.getRight().getBlock() != null)
					.collect(Collectors.toList());
		}
		return Collections.<Pair<BlockPos,IBlockState>>emptyList();
	}
	
	/**
	 * Get all possible blocks that could be part of the multiblock
	 * 
	 * @return Map<BlockPos, IBlockState> of the contents
	 */
	@Override
	public List<Pair<BlockPos, IBlockState>> getStructure() {
		// see public List<> getContents() for a description of how this works
		if (minPos != null && maxPos != null) {
			return StreamSupport.stream(BlockPos.getAllInBox(minPos, maxPos).spliterator(), false)
					.map(pos -> Pair.of(pos, world.getBlockState(pos)))
					.filter(p -> p.getRight() != null && p.getRight().getBlock() != null)
					.collect(Collectors.toList());
		}
		return Collections.<Pair<BlockPos, IBlockState>>emptyList();
	}
}
