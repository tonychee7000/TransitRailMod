package tk.cth451.transitrailmod.blocks.prototype;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PlatformBlock extends CustomDirectionBlock {
	
	public static final PropertyBool UPPER = PropertyBool.create("upper");
	
	public PlatformBlock(Material materialIn) {
		super(materialIn);
	}
	
	// Common Properties
	@Override
	public boolean isTranslucent() {
		return true;
	}
	
	@Override
	public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
	public boolean isFullCube()
    {
        return false;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
	
	public boolean isUpper(IBlockAccess worldIn, BlockPos pos){
		return worldIn.getBlockState(pos.down()).getBlock().equals(this);
	}
	
	// Block States
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(UPPER, isUpper(worldIn, pos));
	}
	
	// Interactions
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		// set facing to the direction player is facing
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		return this.getFacingState(state, placer);
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		BlockPos posToCheck = this.isUpper(worldIn, pos) ? pos.down() : pos.up();
		if (worldIn.getBlockState(posToCheck).getBlock() != this) {
			worldIn.setBlockToAir(pos);
			worldIn.notifyBlockOfStateChange(pos.up(), this);
		}
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}
}
