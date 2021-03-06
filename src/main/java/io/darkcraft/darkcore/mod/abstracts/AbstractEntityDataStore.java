package io.darkcraft.darkcore.mod.abstracts;

import io.darkcraft.darkcore.mod.handlers.entcontainer.EntityContainerHandler;
import io.darkcraft.darkcore.mod.handlers.entcontainer.IEntityContainer;
import io.darkcraft.darkcore.mod.handlers.packets.EntityDataStorePacketHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;

public abstract class AbstractEntityDataStore<E extends EntityLivingBase> implements IExtendedEntityProperties
{
	public final String id;
	private IEntityContainer entity;
	private E temp;

	public AbstractEntityDataStore(E ent, String _id)
	{
		id = _id;
		entity = EntityContainerHandler.getContainer(ent);
		if(entity == null)
			temp = ent;
		EntityDataStorePacketHandler.addStoreType(id);
	}

	public boolean shouldPersistDeaths()
	{
		return true;
	}

	public E getEntity()
	{
		if(entity == null)
		{
			entity = EntityContainerHandler.getContainer(temp);
			if(entity == null)
				return temp;
			else
				temp = null;
		}
		EntityLivingBase e = entity.getEntity();
		return (E) e;
	}

	public void queueUpdate()
	{
		EntityDataStorePacketHandler.queueUpdate(this);
	}

	public boolean sendUpdate()
	{
		return EntityDataStorePacketHandler.sendUpdate(this);
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt)
	{
		writeToNBT(nbt);
		writeTransmittable(nbt);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt)
	{
		readFromNBT(nbt);
		readTransmittable(nbt);
	}

	public abstract void writeToNBT(NBTTagCompound nbt);
	public abstract void readFromNBT(NBTTagCompound nbt);
	public abstract void writeTransmittable(NBTTagCompound nbt);
	public abstract void readTransmittable(NBTTagCompound nbt);

	public abstract boolean notifyArea();

}
