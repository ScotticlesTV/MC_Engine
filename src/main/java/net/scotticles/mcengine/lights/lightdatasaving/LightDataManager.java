package net.scotticles.mcengine.lights.lightdatasaving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.scotticles.mcengine.lights.LightsManager;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LightDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static String fileName = "";
    public static ArrayList<String> lpNames = new ArrayList<>();

    /**
     * Get the save file to use for the world's light data from the server.
     * @param server, the server used to get the world save file.
     * @return The save file as to save/load light data from.
     */
    private static Path getSaveFile(MinecraftServer server) {
        // Get World Folder From Server
        Path worldFolder = server.getSavePath(WorldSavePath.ROOT);
        // return worldFolder.resolve("mcengine/lights.json");
        return worldFolder.resolve("mcengine/lights/" + fileName + ".json");
    }

    public static void getLPNames(MinecraftServer server) {

        Path path = server.getSavePath(WorldSavePath.ROOT).resolve("mcengine/lights/");

        // try-with-resources ensures the system file handles are closed properly
        try (Stream<Path> stream = Files.list(path)) {
            List<String> fileNames = stream
                    .filter(Files::isRegularFile) // Exclude folders
                    .map(p -> p.getFileName().toString().replace(".json", ""))
                    .toList(); // Use .collect(Collectors.toList()) if on Java 16 or older

            lpNames.clear();
            lpNames.addAll(fileNames);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(List<MCEAreaLightData> mceAreaLightDatas, List<MCEPointLightData> mcePointLightDatas, List<MCEDirectionalLightData> mceDirectionalLightDatas, List<MCESpotLightData> mceSpotLightDatas, MinecraftServer server) {
        LightsDataSave save = new LightsDataSave();

        for (MCEAreaLightData mceAreaLightData : mceAreaLightDatas) {
            save.areaLightsData.add(new MCEAreaLightData(
                    mceAreaLightData.getName(),
                    mceAreaLightData.getUuid(),
                    mceAreaLightData.getBrightness(),
                    mceAreaLightData.getColor().red(),
                    mceAreaLightData.getColor().green(),
                    mceAreaLightData.getColor().blue(),
                    mceAreaLightData.getSize().x(),
                    mceAreaLightData.getSize().y(),
                    mceAreaLightData.getPosition().x(),
                    mceAreaLightData.getPosition().y(),
                    mceAreaLightData.getPosition().z(),
                    mceAreaLightData.getOrientation(),
                    mceAreaLightData.getAngle(),
                    mceAreaLightData.getDistance(),
                    mceAreaLightData.isOcclusionEnabled(),
                    mceAreaLightData.getInscatteringStrength()
            ));
        }

        for (MCEPointLightData mcePointLightData : mcePointLightDatas) {
            save.pointLightsData.add(new MCEPointLightData(
                    mcePointLightData.getName(),
                    mcePointLightData.getUuid(),
                    mcePointLightData.getBrightness(),
                    mcePointLightData.getColor().red(),
                    mcePointLightData.getColor().green(),
                    mcePointLightData.getColor().blue(),
                    mcePointLightData.getPosition().x(),
                    mcePointLightData.getPosition().y(),
                    mcePointLightData.getPosition().z(),
                    mcePointLightData.getRadius(),
                    mcePointLightData.isOcclusionEnabled(),
                    mcePointLightData.getInscatteringStrength()
            ));
        }

        for (MCEDirectionalLightData mceDirectionalLightData : mceDirectionalLightDatas) {
            save.directionalLightsData.add(new MCEDirectionalLightData(
                    mceDirectionalLightData.getName(),
                    mceDirectionalLightData.getUuid(),
                    mceDirectionalLightData.getBrightness(),
                    mceDirectionalLightData.getColor().red(),
                    mceDirectionalLightData.getColor().green(),
                    mceDirectionalLightData.getColor().blue(),
                    mceDirectionalLightData.getDirection().x(),
                    mceDirectionalLightData.getDirection().y(),
                    mceDirectionalLightData.getDirection().z(),
                    mceDirectionalLightData.isOcclusionEnabled()
            ));
        }

        for (MCESpotLightData mceSpotLightData : mceSpotLightDatas) {
            save.spotLightsData.add(new MCESpotLightData(
                    mceSpotLightData.getName(),
                    mceSpotLightData.getUuid(),
                    mceSpotLightData.getBrightness(),
                    mceSpotLightData.getColor().red(),
                    mceSpotLightData.getColor().green(),
                    mceSpotLightData.getColor().blue(),
                    mceSpotLightData.getSize(),
                    mceSpotLightData.getPosition().x(),
                    mceSpotLightData.getPosition().y(),
                    mceSpotLightData.getPosition().z(),
                    mceSpotLightData.getOrientation(),
                    mceSpotLightData.getAngle(),
                    mceSpotLightData.getDistance(),
                    mceSpotLightData.isOcclusionEnabled(),
                    mceSpotLightData.getInscatteringStrength()
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

    public static List<MCEAreaLightData> loadAreaLights(MinecraftServer server) {
        Path saveFile = getSaveFile(server);

        List<MCEAreaLightData> areaLights = new ArrayList<>();

        if (!Files.exists(saveFile)) {
            return areaLights;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {

            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);

            if (loaded == null || loaded.areaLightsData == null) {
                return areaLights;
            }

            LightsManager.activeAreaLights.addAll(loaded.areaLightsData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return areaLights;
    }

    public static List<MCEPointLightData> loadPointLights(MinecraftServer server) {
        Path saveFile = getSaveFile(server);

        List<MCEPointLightData> pointLights = new ArrayList<>();

        if (!Files.exists(saveFile)) {
            return pointLights;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {

            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);

            if (loaded == null || loaded.pointLightsData == null) {
                return pointLights;
            }

            LightsManager.activePointLights.addAll(loaded.pointLightsData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return pointLights;
    }

    public static List<MCEDirectionalLightData> loadDirectionalLights(MinecraftServer server) {
        Path saveFile = getSaveFile(server);

        List<MCEDirectionalLightData> directionalLights = new ArrayList<>();

        if (!Files.exists(saveFile)) {
            return directionalLights;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {

            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);

            if (loaded == null || loaded.directionalLightsData == null) {
                return directionalLights;
            }

            LightsManager.activeDirectionalLights.addAll(loaded.directionalLightsData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return directionalLights;
    }

    public static List<MCESpotLightData> loadSpotLights(MinecraftServer server) {
        Path saveFile = getSaveFile(server);

        List<MCESpotLightData> spotLights = new ArrayList<>();

        if (!Files.exists(saveFile)) {
            return spotLights;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {

            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);

            if (loaded == null || loaded.spotLightsData == null) {
                return spotLights;
            }

            LightsManager.activeSpotLights.addAll(loaded.spotLightsData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return spotLights;
    }
}
