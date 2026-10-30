package net.scotticles.mcengine.settings;

import net.fabricmc.loader.api.FabricLoader;
import net.scotticles.mcengine.MCEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MCEngineServerSettings {
    // Config variables
    public static boolean playVanillaMusic = true;
    public static boolean playCustomMusic = false;
    public static String lastLPLoaded = "lights";

    // Config File Reference Inside Fabric Config File
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), MCEngine.MOD_ID + "_server.properties");

    public static void load() {
        Properties properties = new Properties();

        if (CONFIG_FILE.exists()) {
            try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
                properties.load(in);

                // Assign config values from file and set them to defaults if they don't exist
                playVanillaMusic = Boolean.parseBoolean(properties.getProperty("playVanillaMusic", "true"));
                playCustomMusic = Boolean.parseBoolean(properties.getProperty("playCustomMusic", "false"));
                lastLPLoaded = properties.getProperty("lastLPLoaded", "lights");

            } catch (IOException | NumberFormatException e) {
                System.err.println(MCEngine.MOD_ID + " Failed to load config file, using defaults.");
                e.printStackTrace();
            }
        } else {
            // Generate a fresh config file with defaults if it doesn't exist
            save();
        }
    }

    public static void save() {
        Properties properties = new Properties();

        // Put variables into properties object
        properties.setProperty("playVanillaMusic", String.valueOf(playVanillaMusic));
        properties.setProperty("playCustomMusic", String.valueOf(playCustomMusic));
        properties.setProperty("lastLPLoaded", lastLPLoaded);

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, "MC Engine Server Settings\nWARNING - These Are Server-Side Variables, Editing Them On The Client Will Only Work In Singleplayer Worlds / Non-Dedicated Servers");
        } catch (IOException e) {
            System.err.println(MCEngine.MOD_ID + " Failed to save config file.");
            e.printStackTrace();
        }
    }
}