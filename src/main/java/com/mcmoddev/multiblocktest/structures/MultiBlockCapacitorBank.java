package com.mcmoddev.multiblocktest.structures;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.mcmoddev.multiblocktest.init.MyBlocks;
import com.mcmoddev.multiblocktest.util.CuboidMultiblock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("deprecation")
public class MultiBlockCapacitorBank extends CuboidMultiblock {
	private static final List<IBlockState> myBlocks;
	
	static {
		myBlocks = new Stack<>();
		myBlocks.addAll(OreDictionary.getOres("blockIron").stream()
				.map(is -> Pair.of(is.getMetadata(),Block.getBlockFromItem(is.getItem())))
				.map(b -> b.getRight().getStateFromMeta(b.getLeft())).collect(Collectors.toList()));
		myBlocks.addAll(OreDictionary.getOres("blockGlass").stream()
				.map(is -> Pair.of(is.getMetadata(),Block.getBlockFromItem(is.getItem())))
				.map(b -> b.getRight().getStateFromMeta(b.getLeft())).collect(Collectors.toList()));
		myBlocks.addAll(Arrays.asList(MyBlocks.capacitors).stream()
				.map(b -> b.getDefaultState()).collect(Collectors.toList()));
		myBlocks.add(MyBlocks.BLOCK_BANK_CONTROLLER.getDefaultState());
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
