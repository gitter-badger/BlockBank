package me.denyol.blockbank.blocks.vault;

import me.denyol.blockbank.blocks.BlockBase;
import me.denyol.blockbank.blocks.ModBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;

/**
 * Created by Daniel on 27/1/17.
 */
public class BlockSecureCasing extends BlockBase
{
	public BlockSecureCasing(ModBlocks.Blocks block)
	{
		super(block);
		setHarvestLevel("pickaxe", 3);
		setSoundType(SoundType.STONE);
		setHardness(45.0F);
		setResistance(2000.0F);
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.BLOCK;
	}

	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return MapColor.OBSIDIAN;
	}
}
