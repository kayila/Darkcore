package io.darkcraft.darkcore.mod.events;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LightningEvent extends Event
{
	public EntityLightningBolt lightning;

	public LightningEvent(EntityLightningBolt lightning)
	{
		this.lightning = lightning;
	}

}
