package net.scotticles.mcengine.settings;

import net.fabricmc.loader.api.FabricLoader;
import net.scotticles.mcengine.MCEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MCEngineConfig {
    // Config variables
    public static boolean openNavBarOnWorldJoin = false;
    public static boolean useCustomUIColors = false;
    public static float uiColorR = 0;
    public static float uiColorG = 0;
    public static float uiColorB = 0;
    public static float uiColorA = 0;

    // Config File Reference Inside Fabric Config File
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), MCEngine.MOD_ID + ".properties");

    public static void load() {
        Properties properties = new Properties();

        if (CONFIG_FILE.exists()) {
            try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
                properties.load(in);

                // Assign config values from file and set them to defaults if they don't exist
                openNavBarOnWorldJoin = Boolean.parseBoolean(properties.getProperty("openNavBarOnWorldJoin", "false"));
                useCustomUIColors = Boolean.parseBoolean(properties.getProperty("useCustomUIColors", "false"));
                uiColorR = Float.parseFloat(properties.getProperty("uiColorR", "0"));
                uiColorG = Float.parseFloat(properties.getProperty("uiColorG", "0"));
                uiColorB = Float.parseFloat(properties.getProperty("uiColorB", "0"));
                uiColorA = Float.parseFloat(properties.getProperty("uiColorA", "0"));

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
        properties.setProperty("openNavBarOnWorldJoin", String.valueOf(openNavBarOnWorldJoin));
        properties.setProperty("useCustomUIColors", String.valueOf(useCustomUIColors));
        properties.setProperty("uiColorR", String.valueOf(uiColorR));
        properties.setProperty("uiColorB", String.valueOf(uiColorG));
        properties.setProperty("uiColorG", String.valueOf(uiColorB));
        properties.setProperty("uiColorA", String.valueOf(uiColorA));

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, "MC Engine Settings");
        } catch (IOException e) {
            System.err.println(MCEngine.MOD_ID + " Failed to save config file.");
            e.printStackTrace();
        }
    }
}
