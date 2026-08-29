package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.interactions.InteractionsManager;
import net.scotticles.mcengine.interactions.interactiondatasaving.InteractionData;
import net.scotticles.mcengine.ui.UIManager;

import java.util.ArrayList;
import java.util.UUID;

public class InteractionsEditorUI {
    static ImString imInteractionName = new ImString("Interaction", 256);
    static ImInt imInteractionX = new ImInt(0);
    static ImInt imInteractionY = new ImInt(0);
    static ImInt imInteractionZ = new ImInt(0);

    public static void showInteractionEditorUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) return;

            // Track if any text inputs, sliders, or checkboxes were interacted with this frame
            boolean shouldSyncNetwork = false;
            ImGui.begin("Interactions Editor", UIManager.showInteractionEditorUI);
            ImGui.textWrapped("Manage the world's interactions.");

            ImGui.beginTabBar("Interactions");
            // Active Interactions Tab
            if (ImGui.beginTabItem("Active Interactions")) {
                if (ImGui.button("Add Active Interaction")) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        Vec3d playerPos = player.getPos();
                        UUID uuid = UUID.randomUUID();
                        InteractionsManager.addInteraction(uuid, "Interaction", true,
                                (int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ(),
                                new ArrayList<>());
                        // Trigger Sync
                        shouldSyncNetwork = true;
                    }
                }

                int index = 0;
                InteractionData interactionToDelete = null;

                for (InteractionData interactionData : InteractionsManager.activeInteractions) {
                    imInteractionName.set(interactionData.interactionName);
                    imInteractionX.set(interactionData.interactionX);
                    imInteractionY.set(interactionData.interactionY);
                    imInteractionY.set(interactionData.interactionZ);

                    if (interactionData.interactionEnabled) {
                        ImGui.pushID(index);
                        if (ImGui.collapsingHeader(interactionData.interactionName + "###interaction_" + interactionData.interactionUuid)) {

                            ImGui.text("Interaction Name: "); ImGui.sameLine();
                            if (ImGui.inputText("##Interaction Name: ", imInteractionName)) {
                                interactionData.interactionName = imInteractionName.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction Enabled: "); ImGui.sameLine();
                            if (ImGui.checkbox("##Interaction Enabled: ", interactionData.interactionEnabled)) {
                                interactionData.interactionEnabled = !interactionData.interactionEnabled;
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction X: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Interaction X: ", imInteractionX)) {
                                interactionData.interactionX = imInteractionX.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction Y: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Interaction Y: ", imInteractionY)) {
                                interactionData.interactionY = imInteractionY.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction Z: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Interaction Z: ", imInteractionZ)) {
                                interactionData.interactionZ = imInteractionZ.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            if (ImGui.button("Center Interaction On Self")) {
                                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                if (player != null) {
                                    Vec3d playerPos = player.getPos();
                                    interactionData.interactionX = (int) playerPos.getX();
                                    interactionData.interactionY = (int) playerPos.getY();
                                    interactionData.interactionZ = (int) playerPos.getZ();
                                    shouldSyncNetwork = true;
                                }
                            }

                            // Commands
                            ImGui.text("Interaction Commands: ");
                            for (int i = 0; i < interactionData.interactionCommands.size(); i++) {
                                ImGui.pushID(i);
                                ImString Command = new ImString(interactionData.interactionCommands.get(i), 256);
                                if (ImGui.inputText("##Command", Command)) {
                                    interactionData.interactionCommands.set(i, Command.get());
                                }
                                if (ImGui.isItemDeactivatedAfterEdit()) {
                                    shouldSyncNetwork = true;
                                }
                                ImGui.sameLine();
                                if (ImGui.button("X")) {
                                    interactionData.interactionCommands.remove(i);
                                    shouldSyncNetwork = true;
                                }
                                ImGui.popID();
                            }

                            if (ImGui.button("Add Command")) {
                                interactionData.interactionCommands.add("");
                                shouldSyncNetwork = true;
                            }
                            

                            ImGui.separator();
                            if (ImGui.button("Delete Interaction")) {
                                interactionToDelete = interactionData; // Save the interaction data that needs to be deleted to safely delete it outside the loop
                            }
                        }
                        ImGui.popID();
                        index++;
                    }
                }

                if (interactionToDelete != null) {
                    InteractionsManager.activeInteractions.remove(interactionToDelete);
                    shouldSyncNetwork = true;
                }

                ImGui.endTabItem();
            }

            // Disabled Interactions Tab
            if (ImGui.beginTabItem("Disabled Interactions")) {
                if (ImGui.button("Add Disabled Interaction")) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        Vec3d playerPos = player.getPos();
                        UUID uuid = UUID.randomUUID();
                        InteractionsManager.addInteraction(uuid, "Interaction", false,
                                (int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ(),
                                new ArrayList<>());
                        // Trigger Sync
                        shouldSyncNetwork = true;
                    }
                }

                int index = 0;
                InteractionData interactionToDelete = null;

                for (InteractionData interactionData : InteractionsManager.activeInteractions) {
                    imInteractionName.set(interactionData.interactionName);
                    imInteractionX.set(interactionData.interactionX);
                    imInteractionY.set(interactionData.interactionY);
                    imInteractionY.set(interactionData.interactionZ);

                    if (!interactionData.interactionEnabled) {
                        ImGui.pushID(index);
                        if (ImGui.collapsingHeader(interactionData.interactionName + "###region_" + interactionData.interactionUuid)) {

                            ImGui.text("Interaction Name: "); ImGui.sameLine();
                            if (ImGui.inputText("##Interaction Name: ", imInteractionName)) {
                                interactionData.interactionName = imInteractionName.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction Enabled: "); ImGui.sameLine();
                            if (ImGui.checkbox("##Interaction Enabled: ", interactionData.interactionEnabled)) {
                                interactionData.interactionEnabled = !interactionData.interactionEnabled;
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction X: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Interaction X: ", imInteractionX)) {
                                interactionData.interactionX = imInteractionX.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction Y: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Interaction Y: ", imInteractionY)) {
                                interactionData.interactionY = imInteractionY.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Interaction Z: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Interaction Z: ", imInteractionZ)) {
                                interactionData.interactionZ = imInteractionZ.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            if (ImGui.button("Center Region On Self")) {
                                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                if (player != null) {
                                    Vec3d playerPos = player.getPos();
                                    interactionData.interactionX = (int) playerPos.getX();
                                    interactionData.interactionY = (int) playerPos.getY();
                                    interactionData.interactionZ = (int) playerPos.getZ();
                                    shouldSyncNetwork = true;
                                }
                            }

                            // Commands
                            ImGui.text("Interaction Commands: ");
                            for (int i = 0; i < interactionData.interactionCommands.size(); i++) {
                                ImGui.pushID(i);
                                ImString Command = new ImString(interactionData.interactionCommands.get(i), 256);
                                if (ImGui.inputText("##Command", Command)) {
                                    interactionData.interactionCommands.set(i, Command.get());
                                }
                                if (ImGui.isItemDeactivatedAfterEdit()) {
                                    shouldSyncNetwork = true;
                                }
                                ImGui.sameLine();
                                if (ImGui.button("X")) {
                                    interactionData.interactionCommands.remove(i);
                                    shouldSyncNetwork = true;
                                }
                                ImGui.popID();
                            }

                            if (ImGui.button("Add Command")) {
                                interactionData.interactionCommands.add("");
                                shouldSyncNetwork = true;
                            }


                            ImGui.separator();
                            if (ImGui.button("Delete Interaction")) {
                                interactionToDelete = interactionData; // Save the interaction data that needs to be deleted to safely delete it outside the loop
                            }
                        }
                        ImGui.popID();
                        index++;
                    }
                }

                if (interactionToDelete != null) {
                    InteractionsManager.activeInteractions.remove(interactionToDelete);
                    shouldSyncNetwork = true;
                }

                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Mass Interaction Editing")) {
                if (ImGui.button("Enable All Interactions")) {
                    for (InteractionData interactionData : InteractionsManager.activeInteractions) {
                        interactionData.interactionEnabled = true;
                    }
                    shouldSyncNetwork = true;
                }
                if (ImGui.button("Disable All Interactions")) {
                    for (InteractionData interactionData : InteractionsManager.activeInteractions) {
                        interactionData.interactionEnabled = false;
                    }
                    shouldSyncNetwork = true;
                }
                if (ImGui.button("Delete All Interactions")) {
                    InteractionsManager.clearInteractions();
                    shouldSyncNetwork = true;
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
            ImGui.end();


            // Server/Network syncing: Send the client's up to date set of interactions to the server
            if (shouldSyncNetwork) {
                // Sync Interactions To Server And Other Players Via A Packet
                // ClientPlayNetworking.send(new SyncInteractionsDataPayload(new HashSet<>(InteractionsManager.activeInteractions)));
            }
        }
    }
}