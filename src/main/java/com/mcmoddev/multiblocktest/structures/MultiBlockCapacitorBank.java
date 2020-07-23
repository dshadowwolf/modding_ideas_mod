package com.mcmoddev.multiblocktest.structures;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.mcmoddev.multiblocktest.init.MyBlocks;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.util.CuboidMultiblock;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class MultiBlockCapacitorBank extends CuboidMultiblock {
	private static final List<IBlockState> myBlocks;
	private final World world;
	private final BlockPos origin;
	
	static {
		myBlocks = new Stack<>();

		myBlocks.add(Blocks.IRON_BLOCK.getDefaultState());
		myBlocks.add(Blocks.GLASS.getDefaultState());
		for( int i = 0; i < 16; i++) myBlocks.add(Blocks.STAINED_GLASS.getStateFromMeta(i));
		
		myBlocks.addAll(Arrays.asList(MyBlocks.capacitors).stream()
				.map(b -> b.getDefaultState()).collect(Collectors.toList()));
		for(EnumFacing facing : EnumFacing.VALUES)
			myBlocks.add(MyBlocks.BLOCK_BANK_CONTROLLER.getStateFromMeta(facing.getIndex()));
		myBlocks.add(MyBlocks.BLOCK_BANK_INPUT.getDefaultState());
		myBlocks.add(MyBlocks.BLOCK_BANK_OUTPUT.getDefaultState());
	}
	
	public MultiBlockCapacitorBank(BlockPos origin, World worldIn) {
		super(origin, worldIn, 128, 128, 128, myBlocks);
		this.world = worldIn;
		this.origin = origin;
	}

	@Override
	public boolean form() {
		List<TileEntity> t = getTiles();
		TileEntity m = world.getTileEntity(this.origin);
		t.stream()
		  .filter(tex -> tex instanceof ICapacitorComponent)
		  .forEach(te -> ((ICapacitorComponent)te).setMasterComponent((CapBankControllerTile)m));
		return true;
	}
	
	@Override
	public boolean isValidMultiblock() {
		Pair<BlockPos, BlockPos> extents = findLimits();
		List<Pair<BlockPos, IBlockState>> structure = getStructure();
		List<IBlockState> blockStates = structure.stream().map(p -> p.getRight().getBlock().getDefaultState()).collect(Collectors.toList());
		Triple<Integer,Integer,Integer> sz = detectedSize();
		
		// for these first two the tests are also at Y-Min and Y-Max
		// from X-min to X-max at Z-Min and Z-Max should all be Blocks.IRON_BLOCK.getDefaultState()
		List<IBlockState> frameX = structure.stream()
				.filter(p -> p.getLeft().getZ() == extents.getLeft().getZ() || p.getLeft().getZ() == extents.getRight().getZ())
				.filter(p -> p.getLeft().getY() == extents.getLeft().getY() || p.getLeft().getY() == extents.getRight().getY())
				.filter(p -> p.getRight() == Blocks.IRON_BLOCK.getDefaultState())
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		boolean xFrameValid = frameX.size() == (sz.getLeft()*4);
		// from Z-min to Z-max at X-Min and X-Max should all be Blocks.IRON_BLOCK.getDefaultState()
		List<IBlockState> frameZ = structure.stream()
				.filter(p -> p.getLeft().getX() == extents.getLeft().getX() || p.getLeft().getX() == extents.getRight().getX())
				.filter(p -> p.getLeft().getY() == extents.getLeft().getY() || p.getLeft().getY() == extents.getRight().getY())
				.filter(p -> p.getRight() == Blocks.IRON_BLOCK.getDefaultState())
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		boolean zFrameValid = frameZ.size() == (sz.getMiddle()*4);
		// at X-min, X-Max, Z-min and Z-Max at all Y from Y-Min to Y-Max we should have Blocks.IRON_BLOCK
		List<IBlockState> frameY = structure.stream()
				.filter(p -> p.getLeft().getX() == extents.getLeft().getX() || p.getLeft().getX() == extents.getRight().getX())
				.filter(p -> p.getLeft().getZ() == extents.getLeft().getZ() || p.getLeft().getZ() == extents.getRight().getZ())
				.filter(p -> p.getRight() == Blocks.IRON_BLOCK.getDefaultState())
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		boolean yFrameValid = frameY.size() == (sz.getRight()*4);
		List<IBlockState> floor = structure.stream()
				.filter(p -> p.getLeft().getY() == extents.getLeft().getY())
				.filter(p -> myBlocks.contains(p.getRight()) || myBlocks.contains(p.getRight().getBlock().getDefaultState()))
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		List<IBlockState> ceiling = structure.stream()
				.filter(p -> p.getLeft().getY() == extents.getRight().getY())
				.filter(p -> myBlocks.contains(p.getRight()) || myBlocks.contains(p.getRight().getBlock().getDefaultState()))
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		List<IBlockState> zMin = structure.stream()
				.filter(p -> p.getLeft().getZ() == extents.getLeft().getZ())
				.filter(p -> myBlocks.contains(p.getRight()) || myBlocks.contains(p.getRight().getBlock().getDefaultState()))
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		List<IBlockState> zMax = structure.stream()
				.filter(p -> p.getLeft().getZ() == extents.getRight().getZ())
				.filter(p -> myBlocks.contains(p.getRight()) || myBlocks.contains(p.getRight().getBlock().getDefaultState()))
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		List<IBlockState> xMin = structure.stream()
				.filter(p -> p.getLeft().getX() == extents.getLeft().getX())
				.filter(p -> myBlocks.contains(p.getRight()) || myBlocks.contains(p.getRight().getBlock().getDefaultState()))
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		List<IBlockState> xMax = structure.stream()
				.filter(p -> p.getLeft().getX() == extents.getRight().getX())
				.filter(p -> myBlocks.contains(p.getRight()) || myBlocks.contains(p.getRight().getBlock().getDefaultState()))
				.map(p -> p.getRight())
				.collect(Collectors.toList());
		boolean xWallsValid = (xMin.size() == xMax.size()) && (xMax.size() == (sz.getLeft()*sz.getRight()));
		boolean zWallsValid = (zMin.size() == zMax.size()) && (zMax.size() == (sz.getMiddle()*sz.getRight()));
		boolean floorAndCeilingValid = (floor.size() == ceiling.size()) && (ceiling.size() == (sz.getLeft()*sz.getMiddle()));
		
		com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("z Walls Valid: %s -- x Walls Valid: %s -- floorAndCeiling Valid: %s", zWallsValid, xWallsValid, floorAndCeilingValid);
		com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("x Frame Valid: %s -- z Frame Valid: %s -- y Frame Valid: %s", xFrameValid, zFrameValid, yFrameValid);
		com.mcmoddev.multiblocktest.MultiBlockTest.LOGGER.fatal("has right blocks? %s/%s", blockStates.contains(MyBlocks.BLOCK_BANK_OUTPUT.getDefaultState()), blockStates.contains(MyBlocks.BLOCK_BANK_INPUT.getDefaultState()));
		return (xFrameValid && zFrameValid && yFrameValid)  && (xWallsValid && zWallsValid && floorAndCeilingValid) && (blockStates.contains(MyBlocks.BLOCK_BANK_INPUT.getDefaultState()) && blockStates.contains(MyBlocks.BLOCK_BANK_OUTPUT.getDefaultState()));
	}

	@Override
	protected boolean checkMultiblockStructure() {
		// TODO Auto-generated method stub
		return false;
	}
}
