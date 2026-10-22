package net.scotticles.mcengine.regions.regiondatasaving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.scotticles.mcengine.regions.RegionsManager;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegionDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Get the save file to use for the world's region data from the server.
     * @param server, the server used to get the world save file.
     * @return The save file as to save/load region data from.
     */
    private static Path getSaveFile(MinecraftServer server) {
        // Get World Folder From Server
        Path worldFolder = server.getSavePath(WorldSavePath.ROOT);
        return worldFolder.resolve("mcengine/regions.json");
    }

    // Save Regions To A JSON File

    /**
     * Save all region data to the world's save file.
     * @param radialRegions, the actively tracked list of radial regions.
     * @param linearRegions, the actively tracked list of linear regions.
     * @param server, the server used to get the world save file.
     */
    public static void save(List<RadialRegionData> radialRegions, List<LinearRegionData> linearRegions , MinecraftServer server) {
        RegionsDataSave save = new RegionsDataSave();

        for (RadialRegionData radialRegionData : radialRegions) {
            save.radialRegionsData.add(new RadialRegionData(
                    radialRegionData.getUuid(),
                    radialRegionData.getName(),
                    radialRegionData.getX(),
                    radialRegionData.getY(),
                    radialRegionData.getZ(),
                    radialRegionData.getRadius(),
                    radialRegionData.getEnterCommands(),
                    radialRegionData.getExitCommands(),
                    radialRegionData.getPlayersInside(),
                    radialRegionData.isEnabled(),
                    radialRegionData.getSound(),
                    radialRegionData.getSoundVolume(),
                    radialRegionData.getSoundFadeDuration()
            ));
        }

        for (LinearRegionData linearRegionData : linearRegions) {
            save.linearRegionsData.add(new LinearRegionData(
                    linearRegionData.getUuid(),
                    linearRegionData.getName(),
                    linearRegionData.getPos1X(),
                    linearRegionData.getPos1Y(),
                    linearRegionData.getPos1Z(),
                    linearRegionData.getPos2X(),
                    linearRegionData.getPos2Y(),
                    linearRegionData.getPos2Z(),
                    linearRegionData.getEnterCommands(),
                    linearRegionData.getExitCommands(),
                    linearRegionData.getPlayersInside(),
                    linearRegionData.isEnabled(),
                    linearRegionData.getSound(),
                    linearRegionData.getSoundVolume(),
                    linearRegionData.getSoundFadeDuration()
            ));
        }

        Path saveFile = getSaveFile(server);

        try {
            Files.createDirectories(saveFile.getParent());
            try (Writer writer = Files.newBufferedWriter(saveFile)) {
                GSON.toJson(save, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all Radial Region Data objects from a save file.
     * @param server, the server used to get the world save file.
     * @return, the set of Radial Region Data objects
     */
    public static Set<RadialRegionData> loadRadialRegions(MinecraftServer server) {
        Path saveFile = getSaveFile(server);

        Set<RadialRegionData> radialRegions = new HashSet<>();

        if (!Files.exists(saveFile)) {
            return radialRegions;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {

            RegionsDataSave loaded = GSON.fromJson(reader, RegionsDataSave.class);

            if (loaded == null || loaded.radialRegionsData == null) {
                return radialRegions;
            }

            RegionsManager.activeRadialRegions.addAll(loaded.radialRegionsData);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return radialRegions;
    }

    /**
     * Load all Linear Region Data objects from a save file.
     * @param server, the server used to get the world save file.
     * @return, the set of Linear Region Data objects
     */
    public static Set<LinearRegionData> loadLinearRegions(MinecraftServer server) {
        Path saveFile = getSaveFile(server);

        Set<LinearRegionData> linearRegions = new HashSet<>();

        if (!Files.exists(saveFile)) {
            return linearRegions;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {
            RegionsDataSave loaded = GSON.fromJson(reader, RegionsDataSave.class);

            if (loaded == null || loaded.linearRegionsData == null) {
                return linearRegions;
            }

            RegionsManager.activeLinearRegions.addAll(loaded.linearRegionsData);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return linearRegions;
    }
}
