package com.mcmoddev.multiblocktest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.multiblocktest.proxy.Proxy;
import com.mcmoddev.multiblocktest.util.MultiBlockTestConfig;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 *
 */
@Mod(
	modid = MultiBlockTest.MODID,
	name = MultiBlockTest.NAME,
	version = MultiBlockTest.VERSION,
	dependencies = "required-after:forge@[14.23.4.2705,);required-after:mmdlib;required-after:basemetals;",
	useMetadata = false,
	clientSideOnly = false,
	serverSideOnly = false,
	acceptedMinecraftVersions = "[1.12.2]",
	acceptableRemoteVersions = MultiBlockTest.VERSION,
	acceptableSaveVersions = "",
	certificateFingerprint = "@FINGERPRINT@",
	modLanguage = "java",
	modLanguageAdapter = "",
	canBeDeactivated = false,
	guiFactory = "",
	updateJSON = "https://github.com/MinecraftModDevelopmentMods/MultiBlockTest/master/update.json",
	customProperties = {})
public final class MultiBlockTest {

	/* The Mod's Instance. */
	@Mod.Instance
	private static MultiBlockTest instance = null;

	/** ID of this Mod. */
	public static final String MODID = "multiblocktest";

	/** Display name of this Mod. */
	public static final String NAME = "MultiBlock Test Mod";

	/** Version of this Mod. */
	public static final String VERSION = "1.0.0";
//	public static final String VERSION = "@MOD_VERSION@"

	/**
	 *
	 */
	public static final Logger LOGGER = LogManager.getFormatterLogger(MultiBlockTest.MODID);

	/**
	 *
	 */
	private static final class InstanceHolder {

		/**
		 * The Instance.
		 */
		private static final MultiBlockTest INSTANCE = new MultiBlockTest();
	}

	/**
	 *
	 * @return The Mod's Instance.
	 */
	@Mod.InstanceFactory
	public static MultiBlockTest instance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 *
 	 */
	@SidedProxy(
				clientSide = "com.mcmoddev.multiblocktest.proxy.ClientProxy",
				serverSide = "com.mcmoddev.multiblocktest.proxy.ServerProxy")
	private static Proxy proxy = null;

	/**
	 *
 	 * @param event The Event.
	 */
	@Mod.EventHandler
	public static void onFingerprintViolation(final FMLFingerprintViolationEvent event) {
		// This complains if jar not signed, even if certificateFingerprint is blank
		LOGGER.warn("Invalid Fingerprint");
	}

	/**
	 *
 	 * @param event The Event.
	 */
	@Mod.EventHandler
	public static void preInit(final FMLPreInitializationEvent event) {
		// Instead of setting the logger above, you can set it like this instead.
		// logger = event.getModLog()
		MultiBlockTestConfig.init();
		proxy.preInit(event);
	}

	/**
	 *
 	 * @param event The Event.
	 */
	@Mod.EventHandler
	public static void init(final FMLInitializationEvent event) {
		proxy.init(event);
	}

	/**
	 *
 	 * @param event The Event.
	 */
	@Mod.EventHandler
	public static void receiveIMC(final IMCEvent event) {
		proxy.receiveIMC(event);
	}

	/**
	 *
 	 * @param event The Event.
	 */
	@Mod.EventHandler
	public static void postInit(final FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	public static World getWorld() {
		return proxy.getWorld();
	}

	/*
	// Before 1.12
	@Mod.EventHandler
	public static void onRemap(final FMLMissingMappingsEvent event) {
		proxy.onRemap(event);
	}
	*/
}
