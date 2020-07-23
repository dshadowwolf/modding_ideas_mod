package com.mcmoddev.multiblocktest.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.multiblocktest.MultiBlockTest;
import com.mcmoddev.multiblocktest.structures.MultiBlockCapacitorBank;
import com.mcmoddev.multiblocktest.tileentity.CapBankControllerTile;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CapBankController extends MMDBlockWithTile<CapBankControllerTile> {
	private MultiBlockCapacitorBank MULTIBLOCK_DETECTION;
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public CapBankController(Material material) {
		super(CapBankControllerTile.class, CapBankControllerTile::new, material);
		setTranslationKey(MultiBlockTest.MODID + "." + "capacitor_bank_controller");
		setRegistryName(new ResourceLocation(MultiBlockTest.MODID, "capacitor_bank_controller"));
		this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

	@Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase living, ItemStack stack) {
		MULTIBLOCK_DETECTION = new MultiBlockCapacitorBank(pos, world);
		boolean formed = false;
		if(MULTIBLOCK_DETECTION.isValidMultiblock()) {
			formed = true;
			MULTIBLOCK_DETECTION.form();
		} 
		
		if(formed){
			living.sendMessage(new TextComponentString("Multiblock capacitor formed"));
		}else {
			living.sendMessage(new TextComponentString("Invalid Multiblock"));
		}
		super.onBlockPlacedBy(world, pos, state, living, stack);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing enumfacing = facing.getOpposite();

		if(enumfacing == EnumFacing.DOWN) {
			enumfacing = placer.getHorizontalFacing().getOpposite();
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}
}
