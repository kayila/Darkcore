package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public abstract class AbstractWorldDataStore extends WorldSavedData
{
	private final String	name;

	public AbstractWorldDataStore(String _name)
	{
		super(_name);
		name = _name;
	}

	public void load()
	{
		synchronized (this)
		{
			try
			{
				MapStorage data = getData();
				WorldSavedData wsd = data.loadData(getClass(), getName());
				NBTTagCompound nbt = new NBTTagCompound();
				wsd.writeToNBT(nbt);
				readFromNBT(nbt);
			}
			catch (Exception e){}
		}
	}

	public void save()
	{
		synchronized (this)
		{
			try
			{
				MapStorage data = getData();
				data.setData(getName(), this);
			}
			catch (NullPointerException e)
			{
			}
		}
	}

	private MapStorage getData()
	{
		World world = WorldHelper.getWorld(getDimension());
		if (world != null) return world.perWorldStorage;
		return null;
	}

	public String getName()
	{
		return name;
	}

	public abstract int getDimension();

	@Override
	public abstract void readFromNBT(NBTTagCompound nbt);

	@Override
	public abstract void writeToNBT(NBTTagCompound nbt);

}
