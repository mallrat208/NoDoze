package com.mr208.nodoze;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = NoDoze.MOD_ID, name = NoDoze.MOD_NAME, version = NoDoze.MOD_VER)
public class NoDoze {

	public static final String MOD_ID = "nodoze";
	public static final String MOD_NAME = "No Doze";
	public static final String MOD_VER = "1.0.0";

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(SleepEvents.INSTANCE);
	}

	@net.minecraftforge.common.config.Config(modid = MOD_ID)
	public static class Config {

		@net.minecraftforge.common.config.Config.Comment({"If true, players can set their spawn at any point during the day"})
		public static boolean setSpawnDay = true;
	}

	public static class SleepEvents {

		public static SleepEvents INSTANCE = new SleepEvents();

		@SubscribeEvent
		public void onPlayerSleep(PlayerSleepInBedEvent event) {
			EntityPlayer entityPlayer = event.getEntityPlayer();
			World world = event.getEntityPlayer().getEntityWorld();

			event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
			entityPlayer.sendStatusMessage(new TextComponentTranslation("nodoze.sleep.denied"), true);

			if(world.isDaytime() && !Config.setSpawnDay) return;

			if(world.provider.canRespawnHere() && (world.provider.getBiomeForCoords(event.getPos()) != Biomes.HELL)) {
				entityPlayer.setSpawnPoint(event.getPos(), false);
				entityPlayer.setSpawnChunk(event.getPos(), false, event.getEntityPlayer().dimension);

				if(world.isDaytime())
					entityPlayer.sendStatusMessage(new TextComponentTranslation("nodoze.spawn.set"),true);
			}
		}
	}
}
