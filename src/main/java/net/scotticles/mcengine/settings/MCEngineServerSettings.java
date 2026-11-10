package net.scotticles.mcengine.settings;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.scotticles.mcengine.MCEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class MCEngineServerSettings {
    // Config variables
    public static boolean playVanillaMusic = true;
    public static boolean playCustomMusic = false;
    public static String lastLPLoaded = "lights";

    public static void load(MinecraftServer server) {
        Path worldFolder = server.getSavePath(WorldSavePath.ROOT);
        File CONFIG_FILE = worldFolder.resolve("mcengine/server-settings").toFile();


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
            save(server);
        }
    }

    public static void save(MinecraftServer server) {
        Path worldFolder = server.getSavePath(WorldSavePath.ROOT);
        File CONFIG_FILE = worldFolder.resolve("mcengine/server-settings").toFile();

        Properties properties = new Properties();

        // Put variables into properties object
        properties.setProperty("playVanillaMusic", String.valueOf(playVanillaMusic));
        properties.setProperty("playCustomMusic", String.valueOf(playCustomMusic));
        properties.setProperty("lastLPLoaded", lastLPLoaded);

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, "MC Engine Server Settings\nWARNING - Do Not Edit These Variables In File, Please Use The In-Game Editor");
        } catch (IOException e) {
            System.err.println(MCEngine.MOD_ID + " Failed to save config file.");
            e.printStackTrace();
        }
    }
}