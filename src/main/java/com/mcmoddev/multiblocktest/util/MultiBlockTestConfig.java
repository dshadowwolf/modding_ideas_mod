package com.mcmoddev.multiblocktest.util;

import com.mcmoddev.multiblocktest.MultiBlockTest;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 */
@Mod.EventBusSubscriber(modid = MultiBlockTest.MODID)
@Config(modid = MultiBlockTest.MODID, category = "")
public final class MultiBlockTestConfig {

	/**
	 *
	 */
	@Config.LangKey("stat.generalButton")
	public static final General GENERAL = new General();

	/**
	 *
	 */
	public static class General {

		/**
		 *
		 */
		@Config.Comment("Example Boolean")
		public boolean exampleBoolean = false;
	}

	/**
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(MultiBlockTest.MODID)) {
			ConfigManager.sync(MultiBlockTest.MODID, Config.Type.INSTANCE);
		}
	}
}
