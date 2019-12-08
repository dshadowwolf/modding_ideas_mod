package com.mcmoddev.multiblocktest.util;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import com.mcmoddev.multiblocktest.MultiBlockTest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 */
@Mod.EventBusSubscriber(modid = MultiBlockTest.MODID)
public final class MultiBlockTestConfig extends com.mcmoddev.lib.util.Config {

    private static Configuration configuration;
    private static final String CONFIG_FILE = "config/MBTM.cfg";

    public static final Map<String, Map<String, Integer>> config_values = new HashMap<>();
    
    
    
    private static void addConfigItem(String section, String key, int _default) {
    	MultiBlockTest.LOGGER.info(String.format("Asked for %s of section %s with default value %d", key, section, _default));
    	Map<String, Integer> t = config_values.computeIfAbsent(section, m -> new HashMap<>());
    	t.put(key, configuration.getInt(key, section, _default, 0, Integer.MAX_VALUE, "There Is No Comment"));
    	config_values.put(section, t);
    }
    
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
            
            addConfigItem( SharedStrings.CAPACITY, SharedStrings.BASE,        50000 );
            addConfigItem( SharedStrings.CAPACITY, SharedStrings.ADVANCED,   500000 );
            addConfigItem( SharedStrings.CAPACITY, SharedStrings.MASSIVE,   5000000 );
            addConfigItem( SharedStrings.CAPACITY, SharedStrings.FINAL,    10000000 );
            
            addConfigItem( SharedStrings.TRANSMIT, SharedStrings.BASE,         5000 );
            addConfigItem( SharedStrings.TRANSMIT, SharedStrings.ADVANCED,    10000 );
            addConfigItem( SharedStrings.TRANSMIT, SharedStrings.MASSIVE ,    50000 );
            addConfigItem( SharedStrings.TRANSMIT, SharedStrings.FINAL,      100000 );
            
            addConfigItem( SharedStrings.RECEIVE,  SharedStrings.BASE,         5000 );
            addConfigItem( SharedStrings.RECEIVE,  SharedStrings.ADVANCED,    10000 );
            addConfigItem( SharedStrings.RECEIVE,  SharedStrings.MASSIVE,     50000 );
            addConfigItem( SharedStrings.RECEIVE,  SharedStrings.FINAL,      100000 );

            /*
             * Do shit Here
             */

            if (configuration.hasChanged()) {
                    configuration.save();
            }
    }    
}
