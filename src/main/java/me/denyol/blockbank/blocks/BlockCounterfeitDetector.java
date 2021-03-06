/*
 * <BlockBank Minecraft Forge economy mod>
 *     Copyright (C) <2017>  <Daniel Tucker>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.denyol.blockbank.blocks;

import me.denyol.blockbank.BlockBank;
import me.denyol.blockbank.blocks.ModBlocks.Blocks;
import me.denyol.blockbank.network.ModGuiHandler;
import me.denyol.blockbank.tileentity.TileEntityCounterfeitDetector;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCounterfeitDetector extends BlockBase implements ITileEntityProvider
{

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 14.0/16, 1);
	
	public static final PropertyDirection PROPERTY_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockCounterfeitDetector(Blocks block)
	{
		super(block);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(PROPERTY_FACING, EnumFacing.NORTH));
	}
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return MapColor.GRAY;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing facing = EnumFacing.getHorizontal(meta);
		
		return this.getDefaultState().withProperty(PROPERTY_FACING, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumFacing facing = state.getValue(PROPERTY_FACING);

		return facing.getHorizontalIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROPERTY_FACING);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if(placer != null)
		{
			return this.getDefaultState().withProperty(PROPERTY_FACING, placer.getHorizontalFacing().getOpposite());
		}
		
		return this.getDefaultState().withProperty(PROPERTY_FACING, EnumFacing.NORTH);
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityCounterfeitDetector();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityCounterfeitDetector te = (TileEntityCounterfeitDetector) world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, te);
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(stack.hasDisplayName()) // sets the inventory's custom display name if the block stack is named
			((TileEntityCounterfeitDetector) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{	
		if(!worldIn.isRemote)
			playerIn.openGui(BlockBank.instance, ModGuiHandler.GUI.COUNTERFEIT_DETECTOR.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		
		return true;
	}
	
}
