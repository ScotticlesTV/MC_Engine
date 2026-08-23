package net.scotticles.mcengine.interactions.interactiondatasaving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.scotticles.mcengine.interactions.InteractionsManager;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class InteractionDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path getSaveFile(MinecraftServer server) {
        // Get World Folder From Server
        Path worldFolder = server.getSavePath(WorldSavePath.ROOT);
        return worldFolder.resolve("mcengine/interactions.json");
    }

    // Save Interactions To A JSON File
    public static void save(Set<InteractionData> interactions, MinecraftServer server) {
        InteractionsDataSave save = new InteractionsDataSave();

        for (InteractionData interactionData : interactions) {
            save.interactionsData.add(new InteractionData(
                    interactionData.interactionUuid,
                    interactionData.interactionName,
                    interactionData.interactionEnabled,
                    interactionData.interactionX,
                    interactionData.interactionY,
                    interactionData.interactionZ,
                    interactionData.interactionCommands
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


    // Load Interactions From A JSON File
    public static Set<InteractionData> load(MinecraftServer server) {
        Path saveFile = getSaveFile(server);


        Set<InteractionData> interactions = new HashSet<>();

        if (!Files.exists(saveFile)) {
            return interactions;
        }

        try (Reader reader = Files.newBufferedReader(saveFile)) {

            InteractionsDataSave loaded = GSON.fromJson(reader, InteractionsDataSave.class);

            if (loaded == null || loaded.interactionsData == null) {
                return interactions;
            }

            for (InteractionData interaction : loaded.interactionsData) {
                InteractionData interactionData = interaction.toInteractionsData();
                InteractionsManager.activeInteractions.add (interactionData);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return interactions;

    }
}
