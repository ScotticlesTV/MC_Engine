package net.scotticles.mcengine.lights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import foundry.veil.api.client.render.light.data.LightData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.scotticles.mcengine.console.ConsoleManager;
import net.scotticles.mcengine.lights.lightdata.*;
import net.scotticles.mcengine.settings.MCEngineServerSettings;

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

    /**
     * Get the save file to use for the world's light data from the server.
     * @param server, the server used to get the world save file.
     * @return The save file as to save/load light data from.
     */
    private static Path getSaveFile(MinecraftServer server, String fileName) {
        // Get World Folder From Server
        Path worldFolder = server.getSavePath(WorldSavePath.ROOT);
        return worldFolder.resolve("mcengine/lights/" + fileName + ".json");
    }

    /**
     * Get all Light Profile file names from MC Engine's lights folder on the server.
     * @param server, the server to get the Light Profile file names from.
     */
    public static void detectLPNames(MinecraftServer server) {
        Path path = server.getSavePath(WorldSavePath.ROOT).resolve("mcengine/lights/");

        // try-with-resources ensures the system file handles are closed properly
        try (Stream<Path> stream = Files.list(path)) {
            List<String> fileNames = stream
                    .filter(Files::isRegularFile) // Exclude folders
                    .map(p -> p.getFileName().toString().replace(".json", ""))
                    .toList();

            LightsManager.lightProfileNames.clear();
            LightsManager.lightProfileNames.addAll(fileNames);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Save all lights in the world to the specified Light Profile file.
     * @param mceAreaLightDatas, the Area Lights to save.
     * @param mcePointLightDatas, the Point Lights to save.
     * @param mceDirectionalLightDatas, the Directional Lights to save.
     * @param mceSpotLightDatas, the Spot Lights to save.
     * @param server, the server to get the directory from.
     * @param fileName, the name of the file to make at the directory.
     */
    public static boolean saveLP(List<MCEAreaLightData> mceAreaLightDatas, List<MCEPointLightData> mcePointLightDatas, List<MCEDirectionalLightData> mceDirectionalLightDatas, List<MCESpotLightData> mceSpotLightDatas, MinecraftServer server, String fileName) {
        if (fileName.isEmpty()) {
            ConsoleManager.broadcastMessage("Cannot Save Light Profile With No Name", server);
            return false;
        } else {
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

            Path saveFile = getSaveFile(server, fileName);

            try {
                Files.createDirectories(saveFile.getParent());
                try (Writer writer = Files.newBufferedWriter(saveFile)) {
                    GSON.toJson(save, writer);
                    ConsoleManager.broadcastMessage(String.format("Lights Saved To Profile: %s", fileName), server);
                    return true;
                }
            } catch (IOException e) {
                ConsoleManager.broadcastMessage(String.format("Lights Could Not Be Saved To Profile: %s, File Not Accessible", fileName), server);
                return false;
            }
        }
    }

    /**
     * Load all light data and return the all 4 light type lists as one 2D list of lightData.
     * @param server the server to get the light profile directory from.
     * @param fileName the name of the light profile file to load.
     * @return all light data from the light profile file.
     */
    public static List<List<LightData>> loadLP(MinecraftServer server, String fileName) {
        if (!LightsManager.lightProfileNames.contains(fileName)) {

            ConsoleManager.broadcastMessage("No Light Profile Exists By Name: " + fileName, server);
            return null;

        } else if (fileName.isEmpty()) {

            ConsoleManager.broadcastMessage("Cannot Load Light Profile With No Name", server);
            return null;

        } else {
            List<List<LightData>> allLights = new ArrayList<>();

            Path saveFile = getSaveFile(server, fileName);

            //TODO: Consider moving this out of here and into the networking logic so this only happens on a sync call and not server start.
            MCEngineServerSettings.lastLPLoaded = fileName;
            MCEngineServerSettings.save(server);

            try (Reader reader = Files.newBufferedReader(saveFile)) {
                ConsoleManager.broadcastMessage("Light Profile Loaded From: " + fileName, server);

                LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);

                if (loaded == null || loaded.areaLightsData == null || loaded.pointLightsData == null || loaded.directionalLightsData == null || loaded.spotLightsData == null) {
                    return allLights;
                }


                List<LightData> areaLights = new ArrayList<>(loaded.areaLightsData);
                List<LightData> pointLights = new ArrayList<>(loaded.pointLightsData);
                List<LightData> directionalLights = new ArrayList<>(loaded.directionalLightsData);
                List<LightData> spotLights = new ArrayList<>(loaded.spotLightsData);

                allLights.add(areaLights);
                allLights.add(pointLights);
                allLights.add(directionalLights);
                allLights.add(spotLights);

                return allLights;
            }
            catch (IOException ioe) {
                ConsoleManager.broadcastMessage(String.format("Light Profile %s Not Accessible.", fileName), server);
                return null;
            }
        }
    }

    /**
     * Delete the specified Light Profile with the given file name.
     * @param server, the server to get the directory from.
     * @param fileName, the file name of the Light Profile to delete.
     */
    public static boolean deleteLP(MinecraftServer server, String fileName) {
        // Specify the directory and file name
        Path fileToDelete = server.getSavePath(WorldSavePath.ROOT).resolve("mcengine/lights/" + fileName + ".json");

        // Try To Delete Light Profile + Set Message To Send To Connected Players
        if (fileName.isEmpty()) {
            return false;
        } else if (!MCEngineServerSettings.lastLPLoaded.equals(fileName)) {
            try {
                // Deletes the file, returns true if successful, false if file didn't exist
                boolean deleted = Files.deleteIfExists(fileToDelete);

                if (deleted) {
                    ConsoleManager.broadcastMessage(String.format("Light Profile \"%s\" Deleted Successfully", fileName), server);
                    LightsManager.lightProfileNames.remove(fileName);
                    return true;
                } else {
                    ConsoleManager.broadcastMessage(String.format("Cannot Delete Light Profile By Name \"%s\". No Such Profile Exists", fileName), server);
                    return false;
                }
            } catch (IOException e) {
                ConsoleManager.broadcastMessage(String.format("Cannot Not Delete Light Profile \"%s\". File Could Not Be Accessed", fileName), server);
                return false;
            }
        } else {
            ConsoleManager.broadcastMessage("Cannot Delete Currently Opened Light Profile", server);
            return false;
        }
    }

    /**
     *
     * @param server
     * @param fileName
     * @return
     */
//    public static List<MCEAreaLightData> loadAreaLights(MinecraftServer server, String fileName) {
//        Path saveFile = getSaveFile(server, fileName);
//
//        List<MCEAreaLightData> areaLights = new ArrayList<>();
//
//        if (!Files.exists(saveFile)) {
//            return areaLights;
//        }
//
//        try (Reader reader = Files.newBufferedReader(saveFile)) {
//
//            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);
//
//            if (loaded == null || loaded.areaLightsData == null) {
//                return areaLights;
//            }
//
//            LightsManager.activeAreaLights.addAll(loaded.areaLightsData);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return areaLights;
//    }

    /**
     *
     * @param server
     * @param fileName
     * @return
     */
//    public static List<MCEPointLightData> loadPointLights(MinecraftServer server, String fileName) {
//        Path saveFile = getSaveFile(server, fileName);
//
//        List<MCEPointLightData> pointLights = new ArrayList<>();
//
//        if (!Files.exists(saveFile)) {
//            return pointLights;
//        }
//
//        try (Reader reader = Files.newBufferedReader(saveFile)) {
//
//            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);
//
//            if (loaded == null || loaded.pointLightsData == null) {
//                return pointLights;
//            }
//
//            LightsManager.activePointLights.addAll(loaded.pointLightsData);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return pointLights;
//    }

    /**
     *
     * @param server
     * @param fileName
     * @return
     */
//    public static List<MCEDirectionalLightData> loadDirectionalLights(MinecraftServer server, String fileName) {
//        Path saveFile = getSaveFile(server, fileName);
//
//        List<MCEDirectionalLightData> directionalLights = new ArrayList<>();
//
//        if (!Files.exists(saveFile)) {
//            return directionalLights;
//        }
//
//        try (Reader reader = Files.newBufferedReader(saveFile)) {
//
//            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);
//
//            if (loaded == null || loaded.directionalLightsData == null) {
//                return directionalLights;
//            }
//
//            LightsManager.activeDirectionalLights.addAll(loaded.directionalLightsData);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return directionalLights;
//    }

    /**
     * Load all spot lights from the selected file and return them as a list.
     * @param server the server to get the file from.
     * @param fileName the filename to get the lights from.
     * @return the list of spot lights.
     */
//    public static List<MCESpotLightData> loadSpotLights(MinecraftServer server, String fileName) {
//        Path saveFile = getSaveFile(server, fileName);
//
//        List<MCESpotLightData> spotLights = new ArrayList<>();
//
//        if (!Files.exists(saveFile)) {
//            return spotLights;
//        }
//
//        try (Reader reader = Files.newBufferedReader(saveFile)) {
//
//            LightsDataSave loaded = GSON.fromJson(reader, LightsDataSave.class);
//
//            if (loaded == null || loaded.spotLightsData == null) {
//                return spotLights;
//            }
//
//            LightsManager.activeSpotLights.addAll(loaded.spotLightsData);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return spotLights;
//    }
}
