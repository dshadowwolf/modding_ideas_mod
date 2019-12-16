package com.mcmoddev.multiblocktest.structures;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.init.MyBlocks;
import com.mcmoddev.multiblocktest.util.CuboidMultiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockCapacitorBank extends CuboidMultiblock {
	private static final List<IBlockState> myBlocks;
	
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
	}

	@Override
	public boolean form() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidMultiblock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean checkMultiblockStructure() {
		// TODO Auto-generated method stub
		return false;
	}
}
