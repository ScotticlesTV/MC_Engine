package net.scotticles.mcengine.interactions;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.scotticles.mcengine.interactions.interactiondatasaving.InteractionData;
import net.scotticles.mcengine.interactions.interactiondatasaving.InteractionDataManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class InteractionsManager {

    public static final Set<InteractionData> activeInteractions = new HashSet<>();

    public static void interactionsInit() {
        // Load Interactions Data On Server Start
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            loadInteractions(server);
        });

        // Save Interactionss Data On Server Stop
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            saveInteractions(server);
        });

        // Activate Interaction Effects
        InteractionEffectsManager.interactionEffectsInit(activeInteractions);
    }

    private static void loadInteractions(MinecraftServer server) {
        activeInteractions.clear();
        activeInteractions.addAll(InteractionDataManager.load(server));
    }

    private static void saveInteractions(MinecraftServer server) {
        InteractionDataManager.save(activeInteractions, server);
        activeInteractions.clear();
    }

    // Add A New Interaction
    public static void addInteraction(UUID interactionUuid, String interactionName, boolean interactionEnabled, int interactionX, int interactionY, int interactionZ, List<String> interactionCommands) {
        InteractionData interactionData = new InteractionData(interactionUuid, interactionName, interactionEnabled, interactionX, interactionY, interactionZ, interactionCommands);

        activeInteractions.add(interactionData);
        // Needs the Minecraft Server to save
        // InteractionDataManager.save(activeInteractions);
    }

    //Clear All Existing Interactions
    public static void clearInteractions() {
        activeInteractions.clear();
        // Needs the Minecraft Server to save
        // InteractionDataManager.save(activeInteractions);
    }
}
