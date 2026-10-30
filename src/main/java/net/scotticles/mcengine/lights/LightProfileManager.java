package net.scotticles.mcengine.lights;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.WorldSavePath;
import net.scotticles.mcengine.networking.lights.payloads.lightprofiles.SyncLightProfileEditorPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncAreaLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncDirectionalLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncPointLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncSpotLightsDataPayload;
import net.scotticles.mcengine.settings.MCEngineServerSettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LightProfileManager {

    // List of All Light Profile Names
    public static ArrayList<String> lpNames = new ArrayList<>();

    /**
     * Get all Light Profile file names from MC Engine's lights folder on the server.
     * @param server, the server to get the Light Profile file names from.
     */
    public static void getLPNames(MinecraftServer server) {
        Path path = server.getSavePath(WorldSavePath.ROOT).resolve("mcengine/lights/");

        // try-with-resources ensures the system file handles are closed properly
        try (Stream<Path> stream = Files.list(path)) {
            List<String> fileNames = stream
                    .filter(Files::isRegularFile) // Exclude folders
                    .map(p -> p.getFileName().toString().replace(".json", ""))
                    .toList();

            lpNames.clear();
            lpNames.addAll(fileNames);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the specified Light Profile with the given file name.
     * @param server, the server to get the directory from.
     * @param fileName, the file name of the Light Profile to delete.
     */
    public static void deleteLP(MinecraftServer server, String fileName) {
        // Message To Send To All Players After Action Is Completed
        String message = "";
        String newFileName = "";

        // Specify the directory and file name
        Path fileToDelete = server.getSavePath(WorldSavePath.ROOT).resolve("mcengine/lights/" + fileName + ".json");

        // Try To Delete Light Profile + Set Message To Send To Connected Players
        if (fileName.isEmpty()) {
            message = "Cannot Delete Light Profile With No Name";
            newFileName = MCEngineServerSettings.lastLPLoaded;
        } else {
            try {
                // Deletes the file, returns true if successful, false if file didn't exist
                boolean deleted = Files.deleteIfExists(fileToDelete);

                if (deleted) {
                    message = String.format("Light Profile \"%s\" Deleted Successfully", fileName);
                    // Remove The LP Name From The LP Name List If It Was Deleted
                    lpNames.remove(fileName);
                    newFileName = MCEngineServerSettings.lastLPLoaded;
                    // If The Currently Active Profile Was Deleted, Load The First Profile In The List And Sync To All Clients
                    if (MCEngineServerSettings.lastLPLoaded.equals(fileName)) {
                        newFileName = lpNames.getFirst();
                        LightsManager.loadLights(server, newFileName);
                        LightsManager.syncAllLights();
                        MCEngineServerSettings.lastLPLoaded = newFileName;
                        MCEngineServerSettings.save();
                    }
                } else {
                    message = String.format("Cannot Delete Light Profile By Name \"%s\", No Such Profile Exists", fileName);
                    newFileName = MCEngineServerSettings.lastLPLoaded;
                }
            } catch (IOException e) {
                message = "Cannot Access Light Profile \"%s\", Light Profile Not Deleted";
                newFileName = MCEngineServerSettings.lastLPLoaded;
            }
        }

        // Send A Sync Packet To All Players On The Server
        SyncLightProfileEditorPayload syncPayload = new SyncLightProfileEditorPayload(lpNames, newFileName, message);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, syncPayload);
        }
    }

    public static void newLP(MinecraftServer server) {

    }
}
