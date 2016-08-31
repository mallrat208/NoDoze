package com.mr208.nodoze;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = NoDoze.MODID, name = NoDoze.MODNAME, version = NoDoze.MODVERSION)
public class NoDoze {

	public static final String MODID = "nodoze";
	public static final String MODNAME = "No Doze";
	public static final String MODVERSION = "1.0.0";

	public static Configuration config;
	public static boolean setSpawnDay;

	@Mod.Instance(MODID)
	public static NoDoze instance = new NoDoze();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		setSpawnDay = config.get("Settings","SetSpawnAnyTime",true).getBoolean();
		config.save();

	}

	@SubscribeEvent
	public void onPlayerSleep(PlayerSleepInBedEvent event)
	{
		event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);

		World world = event.getEntityPlayer().getEntityWorld();

		if(world.isDaytime() && !setSpawnDay) return;

		if(world.provider.canRespawnHere() && world.provider.getBiomeForCoords(event.getPos()) != Biomes.HELL)
		{
			EntityPlayer entityPlayer = event.getEntityPlayer();
			entityPlayer.setSpawnPoint(event.getPos(), false);
			entityPlayer.setSpawnChunk(event.getPos(), false, event.getEntityPlayer().dimension);
			entityPlayer.addChatComponentMessage(new TextComponentTranslation("nodoze.spawn.set"));
		}

	}
}
