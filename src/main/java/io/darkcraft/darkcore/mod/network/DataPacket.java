package io.darkcraft.darkcore.mod.network;

import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class DataPacket extends FMLProxyPacket
{
	public PacketBuffer buffer;

	public DataPacket(PacketBuffer payload, String channel)
	{
		super(payload, "darkcore");
		buffer = payload;
	}

	public DataPacket(PacketBuffer payload)
	{
		super(payload, "darkcore");
		buffer = payload;
	}

	public DataPacket(PacketBuffer payload, NBTTagCompound nbt, String id)
	{
		super(payload, "darkcore");
		byte[] bytes = id.getBytes();
		if(bytes.length > 255)
			throw new RuntimeException("Invalid network id - " + id + " - too long");
		payload.writeByte((byte)id.length());
		payload.writeBytes(bytes);
		payload.writerIndex(1+bytes.length);
		try
		{
			ByteBufOutputStream stream = new ByteBufOutputStream(payload);
			ServerHelper.writeNBT(nbt, stream);
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public DataPacket(NBTTagCompound nbt, String id)
	{
		this(new PacketBuffer(Unpooled.buffer()), nbt, id);
	}

	public NBTTagCompound getNBT()
	{
		ByteBufInputStream in = new ByteBufInputStream(buffer);
		NBTTagCompound nbt = ServerHelper.readNBT(in);
		try
		{
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return nbt;
	}

}
