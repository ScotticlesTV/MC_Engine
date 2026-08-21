package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;
import net.scotticles.mcengine.ui.UIManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class RegionsEditorUI {
    static ImString imRegionName = new ImString("Region", 256);
    static ImInt imRegionX = new ImInt(0);
    static ImInt imRegionY = new ImInt(0);
    static ImInt imRegionZ = new ImInt(0);
    static ImInt imRegionRadius = new ImInt(0);
    static ImString imRegionSound = new ImString("", 256);
    static ImFloat imRegionSoundVolume = new ImFloat(1.0f);
    static ImFloat imRegionSoundFadeDuration = new ImFloat(2.0f);

    public static void showRegionsEditorUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) return;

            // Track if any text inputs, sliders, or checkboxes were interacted with this frame
            boolean shouldSyncNetwork = false;
            ImGui.begin("Regions Editor", UIManager.showRegionEditorUI);
            ImGui.textWrapped("Manage the world's regions.");

            ImGui.beginTabBar("Regions");
            // Active Regions Tab
            if (ImGui.beginTabItem("Active Regions")) {
                if (ImGui.button("Add Active Region")) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        Vec3d playerPos = player.getPos();
                        UUID uuid = UUID.randomUUID();
                        RegionsManager.addRegion(uuid, "Region",
                                (int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ(),
                                20, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), true, "", 1.0f, 2.0f);
                        // Trigger Sync
                        shouldSyncNetwork = true;
                    }
                }

                int index = 0;
                RegionData regionToDelete = null;

                for (RegionData regionData : RegionsManager.activeRegions) {
                    imRegionName.set(regionData.regionName);
                    imRegionX.set(regionData.regionX);
                    imRegionY.set(regionData.regionY);
                    imRegionZ.set(regionData.regionZ);
                    imRegionRadius.set(regionData.regionRadius);
                    imRegionSound.set(regionData.regionSound);
                    imRegionSoundVolume.set(regionData.regionSoundVolume);
                    imRegionSoundFadeDuration.set(regionData.regionSoundFadeDuration);

                    if (regionData.regionEnabled) {
                        ImGui.pushID(index);
                        if (ImGui.collapsingHeader(regionData.regionName + "###region_" + regionData.regionUuid)) {

                            ImGui.text("Region Name: "); ImGui.sameLine();
                            if (ImGui.inputText("##Region Name: ", imRegionName)) {
                                regionData.regionName = imRegionName.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Enabled: "); ImGui.sameLine();
                            if (ImGui.checkbox("##Region Enabled: ", regionData.regionEnabled)) {
                                regionData.regionEnabled = !regionData.regionEnabled;
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region X: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region X: ", imRegionX)) {
                                regionData.regionX = imRegionX.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Y: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Y: ", imRegionY)) {
                                regionData.regionY = imRegionY.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Z: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Z: ", imRegionZ)) {
                                regionData.regionZ = imRegionZ.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            if (ImGui.button("Center Region On Self")) {
                                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                if (player != null) {
                                    Vec3d playerPos = player.getPos();
                                    regionData.regionX = (int) playerPos.getX();
                                    regionData.regionY = (int) playerPos.getY();
                                    regionData.regionZ = (int) playerPos.getZ();
                                    shouldSyncNetwork = true;
                                }
                            }

                            ImGui.text("Region Radius: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Radius: ", imRegionRadius)) {
                                regionData.regionRadius = imRegionRadius.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            // Enter Commands
                            ImGui.text("Region Enter Commands: ");
                            for (int i = 0; i < regionData.regionEnterCommands.size(); i++) {
                                ImGui.pushID(i);
                                ImString enterCommand = new ImString(regionData.regionEnterCommands.get(i), 256);
                                if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                    regionData.regionEnterCommands.set(i, enterCommand.get());
                                }
                                if (ImGui.isItemDeactivatedAfterEdit()) {
                                    shouldSyncNetwork = true;
                                }
                                ImGui.sameLine();
                                if (ImGui.button("X")) {
                                    regionData.regionEnterCommands.remove(i);
                                    shouldSyncNetwork = true;
                                }
                                ImGui.popID();
                            }

                            if (ImGui.button("Add Enter Command")) {
                                regionData.regionEnterCommands.add("");
                                shouldSyncNetwork = true;
                            }

                            // Exit Commands
                            ImGui.text("Region Exit Commands: ");
                            for (int i = 0; i < regionData.regionExitCommands.size(); i++) {
                                ImGui.pushID(i);
                                ImString exitCommand = new ImString(regionData.regionExitCommands.get(i), 256);
                                if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                    regionData.regionExitCommands.set(i, exitCommand.get());
                                }
                                if (ImGui.isItemDeactivatedAfterEdit()) {
                                    shouldSyncNetwork = true;
                                }
                                ImGui.sameLine();
                                if (ImGui.button("X")) {
                                    regionData.regionExitCommands.remove(i);
                                    shouldSyncNetwork = true;
                                }
                                ImGui.popID();
                            }

                            if (ImGui.button("Add Exit Command")) {
                                regionData.regionExitCommands.add("");
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Sound: "); ImGui.sameLine();
                            if (ImGui.inputText("##RegionSound", imRegionSound)) {
                                regionData.regionSound = imRegionSound.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                            if (ImGui.inputFloat("##Region Sound Volume: ", imRegionSoundVolume)) {
                                regionData.regionSoundVolume = imRegionSoundVolume.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                            if (ImGui.inputFloat("##Region Sound Fade Duration: ", imRegionSoundFadeDuration)) {
                                regionData.regionSoundFadeDuration = imRegionSoundFadeDuration.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.separator();
                            if (ImGui.button("Delete Region")) {
                                regionToDelete = regionData; // Save the region data that needs to be deleted to safely delete it outside the loop
                            }
                        }
                        ImGui.popID();
                        index++;
                    }
                }

                if (regionToDelete != null) {
                    RegionsManager.activeRegions.remove(regionToDelete);
                    shouldSyncNetwork = true;
                }

                ImGui.endTabItem();
            }

            // Disabled Regions Tab
            if (ImGui.beginTabItem("Disabled Regions")) {
                if (ImGui.button("Add Disabled Region")) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        Vec3d playerPos = player.getPos();
                        UUID uuid = UUID.randomUUID();
                        RegionsManager.addRegion(uuid,"Region",
                                (int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ(),
                                20, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), false, "", 1, 2);
                        shouldSyncNetwork = true; // Trigger Sync
                    }
                }

                int index = 0;
                RegionData regionToDelete = null;

                for (RegionData regionData : RegionsManager.activeRegions) {
                    imRegionName.set(regionData.regionName);
                    imRegionX.set(regionData.regionX);
                    imRegionY.set(regionData.regionY);
                    imRegionZ.set(regionData.regionZ);
                    imRegionRadius.set(regionData.regionRadius);
                    imRegionSoundVolume.set(regionData.regionSoundVolume);
                    imRegionSoundFadeDuration.set(regionData.regionSoundFadeDuration);

                    if (!regionData.regionEnabled) {
                        ImGui.pushID(index);
                        if (ImGui.collapsingHeader(regionData.regionName + "###region_" + index)) {

                            ImGui.text("Region Name: "); ImGui.sameLine();
                            if (ImGui.inputText("##Region Name: ", imRegionName)) {
                                regionData.regionName = imRegionName.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Enabled: "); ImGui.sameLine();
                            if (ImGui.checkbox("##Region Enabled: ", regionData.regionEnabled)) {
                                regionData.regionEnabled = !regionData.regionEnabled;
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region X: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region X: ", imRegionX)) {
                                regionData.regionX = imRegionX.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Y: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Y: ", imRegionY)) {
                                regionData.regionY = imRegionY.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Z: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Z: ", imRegionZ)) {
                                regionData.regionZ = imRegionZ.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            if (ImGui.button("Center Region On Self")) {
                                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                if (player != null) {
                                    Vec3d playerPos = player.getPos();
                                    regionData.regionX = (int) playerPos.getX();
                                    regionData.regionY = (int) playerPos.getY();
                                    regionData.regionZ = (int) playerPos.getZ();
                                    shouldSyncNetwork = true;
                                }
                            }

                            // Region Radius
                            ImGui.text("Region Radius: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Radius: ", imRegionRadius)) {
                                regionData.regionRadius = imRegionRadius.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            // Enter Commands
                            ImGui.text("Region Enter Commands: ");
                            for (int i = 0; i < regionData.regionEnterCommands.size(); i++) {
                                ImGui.pushID(i);
                                ImString enterCommand = new ImString(regionData.regionEnterCommands.get(i), 256);
                                if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                    regionData.regionEnterCommands.set(i, enterCommand.get());
                                    shouldSyncNetwork = true;
                                }
                                ImGui.sameLine();
                                if (ImGui.button("X")) {
                                    regionData.regionEnterCommands.remove(i);
                                    shouldSyncNetwork = true;
                                }
                                ImGui.popID();
                            }
                            if (ImGui.button("Add Enter Command")) {
                                regionData.regionEnterCommands.add("");
                                shouldSyncNetwork = true;
                            }
                            // Exit Commands
                            ImGui.text("Region Exit Commands: ");
                            for (int i = 0; i < regionData.regionExitCommands.size(); i++) {
                                ImGui.pushID(i);
                                ImString exitCommand = new ImString(regionData.regionExitCommands.get(i), 256);
                                if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                    regionData.regionExitCommands.set(i, exitCommand.get());
                                }
                                if (ImGui.isItemDeactivatedAfterEdit()) {
                                    shouldSyncNetwork = true;
                                }
                                ImGui.sameLine();
                                if (ImGui.button("X")) {
                                    regionData.regionExitCommands.remove(i);
                                    shouldSyncNetwork = true;
                                }
                                ImGui.popID();
                            }
                            if (ImGui.button("Add Exit Command")) {
                                regionData.regionExitCommands.add("");
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Sound: "); ImGui.sameLine();
                            if (ImGui.inputText("##RegionSound", imRegionSound)) {
                                regionData.regionSound = imRegionSound.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                            if (ImGui.inputFloat("##Region Sound Volume: ", imRegionSoundVolume)) {
                                regionData.regionSoundVolume = imRegionSoundVolume.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                            if (ImGui.inputFloat("##Region Sound Volume: ", imRegionSoundFadeDuration)) {
                                regionData.regionSoundFadeDuration = imRegionSoundFadeDuration.get();
                            }
                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                shouldSyncNetwork = true;
                            }

                            ImGui.separator();
                            if (ImGui.button("Delete Region")) {
                                // Save the region data that needs to be deleted to safely delete it outside the loop
                                regionToDelete = regionData;
                            }
                        }
                        ImGui.popID();
                        index++;
                    }
                }

                if (regionToDelete != null) {
                    RegionsManager.activeRegions.remove(regionToDelete);
                    shouldSyncNetwork = true;
                }

                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Mass Region Editing")) {
                if (ImGui.button("Enable All Regions")) {
                    for (RegionData regionData : RegionsManager.activeRegions) {
                        regionData.regionEnabled = true;
                    }
                    shouldSyncNetwork = true;
                }
                if (ImGui.button("Disable All Regions")) {
                    for (RegionData regionData : RegionsManager.activeRegions) {
                        regionData.regionEnabled = false;
                    }
                    shouldSyncNetwork = true;
                }
                if (ImGui.button("Delete All Regions")) {
                    RegionsManager.clearRegions();
                    shouldSyncNetwork = true;
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
            ImGui.end();


            // Server/Network syncing: Send the client's up to date set of regions to the server
            if (shouldSyncNetwork) {
                ClientPlayNetworking.send(new SyncRegionsDataPayload(new HashSet<>(RegionsManager.activeRegions)));
            }
        }
    }
}