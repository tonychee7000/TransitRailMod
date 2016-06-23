package tk.cth451.transitrailmod.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tk.cth451.transitrailmod.TransitRailMod;
import tk.cth451.transitrailmod.init.ModBlocks;

public class WirePanel extends Block  {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool LAMP = PropertyBool.create("lamp");
	
	public WirePanel(Material materialIn) {
		super(Material.iron);
		this.setUnlocalizedName("wire_panel");
		this.setCreativeTab(TransitRailMod.tabTransitRail);
	}
	
	// Properties
	@Override
	public int getMobilityFlag()
    {
        return 1;
    }
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isFullCube()
    {
        return false;
    }
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = (EnumFacing) worldIn.getBlockState(pos).getValue(FACING); 
		if (facing == EnumFacing.NORTH) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
		} else if (facing == EnumFacing.EAST) {
			this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else if (facing == EnumFacing.SOUTH) {
			this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
		} else { // WEST
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list,
			Entity collidingEntity) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}
	
	// Block States
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {FACING, LAMP});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int mFacing = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		return mFacing;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing pFacing = EnumFacing.getHorizontal(meta);
		return this.getDefaultState().withProperty(FACING, pFacing);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean isLampPresent = worldIn.getBlockState(pos.up()).getBlock() == ModBlocks.fluorescent_lamp;
		return state.withProperty(LAMP, isLampPresent);
	}
	
	// Interactions
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		// set facing to the direction player is facing
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		EnumFacing thisFacing = placer.getHorizontalFacing();
		return this.getActualState(state, worldIn, pos).withProperty(FACING, thisFacing);
	}
}
