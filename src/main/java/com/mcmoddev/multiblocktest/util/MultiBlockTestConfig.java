package com.mcmoddev.multiblocktest.util;

import java.io.File;

import com.mcmoddev.multiblocktest.MultiBlockTest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 */
@Mod.EventBusSubscriber(modid = MultiBlockTest.MODID)
@Config(modid = MultiBlockTest.MODID, category = "")
public final class MultiBlockTestConfig extends com.mcmoddev.lib.util.Config {

    private static Configuration configuration;
    private static final String CONFIG_FILE = "config/MBTM.cfg";
    private static final String BLARGH = "!BLARGH!";

    /**
     *
     * @param event
     *            The Event.
     */
    @SubscribeEvent
    public void onConfigChange(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MultiBlockTest.MODID)) {
                    init();
            }
    }

    /**
     *
     */
    public static void init() {
            if (configuration == null) {
                    configuration = new Configuration(new File(CONFIG_FILE));
                    MinecraftForge.EVENT_BUS.register(new MultiBlockTestConfig());
            }
            /*
             * Do shit Here
             */

            if (configuration.hasChanged()) {
                    configuration.save();
            }
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
