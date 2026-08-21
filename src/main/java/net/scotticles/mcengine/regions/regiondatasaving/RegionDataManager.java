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
import java.util.Set;

public class RegionDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path getSaveFile(MinecraftServer server) {
        // Get World Folder From Server
        Path worldFolder = server.getSavePath(WorldSavePath.ROOT);
        return worldFolder.resolve("mcengine/regions.json");
    }

    // Save Regions To A JSON File
    public static void save(Set<RegionData> regions, MinecraftServer server) {
        RegionsDataSave save = new RegionsDataSave();

        for (RegionData regionData : regions) {
            save.regionsData.add(new RegionData(
                    regionData.regionUuid,
                    regionData.regionName,
                    regionData.regionX,
                    regionData.regionY,
                    regionData.regionZ,
                    regionData.regionRadius,
                    regionData.regionEnterCommands,
                    regionData.regionExitCommands,
                    regionData.playersInside,
                    regionData.regionEnabled,
                    regionData.regionSound,
                    regionData.regionSoundVolume,
                    regionData.regionSoundFadeDuration
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


    // Load Regions From A JSON File
    public static Set<RegionData> load(MinecraftServer server) {
        Path saveFile = getSaveFile(server);


        Set<RegionData> regions = new HashSet<>();

        if (!Files.exists(saveFile)) {
            return regions;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {

            RegionsDataSave loaded = GSON.fromJson(reader, RegionsDataSave.class);

            if (loaded == null || loaded.regionsData == null) {
                return regions;
            }

            for (RegionData region : loaded.regionsData) {
                RegionData regionData = region.toRegionsData();
                RegionsManager.activeRegions.add (regionData);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return regions;

    }
}
