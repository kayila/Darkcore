package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.interfaces.IColorableBlock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractItemBlock extends ItemBlock
{
	Block	bID;

	public AbstractItemBlock(Block par1)
	{
		super(par1);
		bID = par1;
		if (!(getBlock() instanceof IColorableBlock))
			setHasSubtypes(true);
		else
			setHasSubtypes(false);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		AbstractBlock block = getBlock();
		if (block != null) { return block.getUnlocalizedName(itemStack.getItemDamage()); }
		return bID.getUnlocalizedName();
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		return this.placeBlockAt(stack, player, world, new BlockPos(x, y, z), side, hitX, hitY, hitZ, state);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state)
	{
		if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state))
		{
			NBTTagCompound tag = stack.getTagCompound();
			if (tag != null)
			{
				if (tag.hasKey("x"))
				{
					tag.removeTag("x");
					tag.removeTag("y");
					tag.removeTag("z");
					tag.removeTag("id");
				}
				tag.setBoolean("placed", true);
				TileEntity te = world.getTileEntity(pos);
				if (te != null) te.readFromNBT(tag);
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public void addInfo(ItemStack is, EntityPlayer player, List infoList)
	{
	}

	@SuppressWarnings("rawtypes")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List infoList, boolean par4)
	{
		super.addInformation(is, player, infoList, par4);
		addInfo(is, player, infoList);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack is, int s)
	{
		if (is == null) return 16777215;
		int m = is.getItemDamage();
		Block b = getBlock();
		if (b instanceof IColorableBlock)
		{
			if ((m >= 0) && (m < ItemDye.DYE_COLORS.length)) return ItemDye.DYE_COLORS[m];
		}
		return 16777215;
	}

}
