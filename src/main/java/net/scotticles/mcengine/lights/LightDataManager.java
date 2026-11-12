package net.scotticles.mcengine.lights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import foundry.veil.api.client.render.light.data.LightData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
import net.scotticles.mcengine.lights.lightdata.*;
import net.scotticles.mcengine.networking.lights.payloads.lightprofiles.SyncLightProfileEditorPayload;
import net.scotticles.mcengine.settings.MCEngineServerSettings;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
     * Save all lights in the world to the specified Light Profile file.
     * @param mceAreaLightDatas, the Area Lights to save.
     * @param mcePointLightDatas, the Point Lights to save.
     * @param mceDirectionalLightDatas, the Directional Lights to save.
     * @param mceSpotLightDatas, the Spot Lights to save.
     * @param server, the server to get the directory from.
     * @param fileName, the name of the file to make at the directory.
     */
    public static void save(List<MCEAreaLightData> mceAreaLightDatas, List<MCEPointLightData> mcePointLightDatas, List<MCEDirectionalLightData> mceDirectionalLightDatas, List<MCESpotLightData> mceSpotLightDatas, MinecraftServer server, String fileName) {
        // Message To Send To All Players After Action Is Completed
        String message = "";

        if (fileName.isEmpty()) {
            message = "Cannot Save Light Profile With No Name";
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
                    message = String.format("Lights Saved To Profile: %s", fileName);
                    LightProfilesManager.getLPNames(server);
                }
            } catch (IOException e) {
                message = String.format("Lights Could Not Be Saved To Profile: %s, File Not Accessible", fileName);
            }
        }

        SyncLightProfileEditorPayload syncPayload = new SyncLightProfileEditorPayload(LightProfilesManager.lpNames, MCEngineServerSettings.lastLPLoaded, message);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, syncPayload);
        }
    }

    /**
     *
     * @param server
     * @param fileName
     * @return
     */
    public static List<List<LightData>> load(MinecraftServer server, String fileName) {
        List<List<LightData>> allLights = new ArrayList<>();
        String message = "";
        String newFileName = "";
        if (!LightProfilesManager.lpNames.contains(fileName)) {

            message = "No Light Profile Exists By Name: " + fileName;
            newFileName = MCEngineServerSettings.lastLPLoaded;

        } else if (fileName.isEmpty()) {

            message = "Cannot Load Light Profile With No Name";
            newFileName = MCEngineServerSettings.lastLPLoaded;

        } else {

            Path saveFile = getSaveFile(server, fileName);


            MCEngineServerSettings.lastLPLoaded = fileName;
            MCEngineServerSettings.save(server);



            try (Reader reader = Files.newBufferedReader(saveFile)) {
                message = "Light Profile Loaded From: " + fileName;
                newFileName = fileName;
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
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        SyncLightProfileEditorPayload syncPayload = new SyncLightProfileEditorPayload(LightProfilesManager.lpNames, newFileName, message);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, syncPayload);
        }

        return allLights;

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
