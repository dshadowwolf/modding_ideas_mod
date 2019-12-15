package com.mcmoddev.multiblocktest.blocks;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.structures.MultiBlockCapacitorBank;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;
import com.mcmoddev.multiblocktest.util.ICapacitorComponent;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CapBankController extends MMDBlockWithTile<CapBankControllerTile> {
	private MultiBlockCapacitorBank MULTIBLOCK_DETECTION;
	private List<TileEntity> subsidiaries;
	public static final PropertyDirection FACING = PropertyDirection.create("facing", (@Nullable EnumFacing input) -> input != EnumFacing.DOWN);
	
	public CapBankController(Material material) {
		super(CapBankControllerTile.class, CapBankControllerTile::new, material);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase living, ItemStack stack) {
		MULTIBLOCK_DETECTION = new MultiBlockCapacitorBank(pos, world);
		MultiBlockTest.LOGGER.info("Multiblock Detected: %s --> isValid: %s", MULTIBLOCK_DETECTION.detectMultiblock()?"YES":"NO", MULTIBLOCK_DETECTION.isValidMultiblock()?"YES":"NO");
		if(MULTIBLOCK_DETECTION.isValidMultiblock()) {
			this.subsidiaries = MULTIBLOCK_DETECTION.getTiles().stream().filter(te -> (te instanceof ICapacitorComponent)).collect(Collectors.toList());
			this.subsidiaries.forEach(sub -> ((ICapacitorComponent)sub).setMasterComponent(this));
		}
		super.onBlockPlacedBy(world, pos, state, living, stack);
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing enumfacing = facing.getOpposite();

		if(enumfacing == EnumFacing.DOWN) {
			enumfacing = placer.getHorizontalFacing().getOpposite();
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}
}
