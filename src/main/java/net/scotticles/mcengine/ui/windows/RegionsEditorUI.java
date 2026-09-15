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
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;
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
            // Radial Regions Tab
            // Features:
            //  - Regions Centered Around A Single Point With A Radial Effect Area
            if (ImGui.beginTabItem("Radial Regions")) {
                if (ImGui.beginTabBar("Radial Regions Tab Bar")) {
                    if (ImGui.beginTabItem("Active")) {
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
                        RadialRegionData regionToDelete = null;

                        for (RadialRegionData radialRegionData : RegionsManager.activeRegions) {
                            imRegionName.set(radialRegionData.regionName);
                            imRegionX.set(radialRegionData.regionX);
                            imRegionY.set(radialRegionData.regionY);
                            imRegionZ.set(radialRegionData.regionZ);
                            imRegionRadius.set(radialRegionData.regionRadius);
                            imRegionSound.set(radialRegionData.regionSound);
                            imRegionSoundVolume.set(radialRegionData.regionSoundVolume);
                            imRegionSoundFadeDuration.set(radialRegionData.regionSoundFadeDuration);

                            if (radialRegionData.regionEnabled) {
                                ImGui.pushID(index);
                                if (ImGui.collapsingHeader(radialRegionData.regionName + "###region_" + radialRegionData.regionUuid)) {

                                    ImGui.text("Region Name: "); ImGui.sameLine();
                                    if (ImGui.inputText("##Region Name: ", imRegionName)) {
                                        radialRegionData.regionName = imRegionName.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Enabled: "); ImGui.sameLine();
                                    if (ImGui.checkbox("##Region Enabled: ", radialRegionData.regionEnabled)) {
                                        radialRegionData.regionEnabled = !radialRegionData.regionEnabled;
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region X: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X: ", imRegionX)) {
                                        radialRegionData.regionX = imRegionX.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y: ", imRegionY)) {
                                        radialRegionData.regionY = imRegionY.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z: ", imRegionZ)) {
                                        radialRegionData.regionZ = imRegionZ.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            radialRegionData.regionX = (int) playerPos.getX();
                                            radialRegionData.regionY = (int) playerPos.getY();
                                            radialRegionData.regionZ = (int) playerPos.getZ();
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    ImGui.text("Region Radius: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Radius: ", imRegionRadius)) {
                                        radialRegionData.regionRadius = imRegionRadius.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    // Enter Commands
                                    ImGui.text("Region Enter Commands: ");
                                    for (int i = 0; i < radialRegionData.regionEnterCommands.size(); i++) {
                                        ImGui.pushID(i);
                                        ImString enterCommand = new ImString(radialRegionData.regionEnterCommands.get(i), 256);
                                        if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                            radialRegionData.regionEnterCommands.set(i, enterCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            radialRegionData.regionEnterCommands.remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }

                                    if (ImGui.button("Add Enter Command")) {
                                        radialRegionData.regionEnterCommands.add("");
                                        shouldSyncNetwork = true;
                                    }

                                    // Exit Commands
                                    ImGui.text("Region Exit Commands: ");
                                    for (int i = 0; i < radialRegionData.regionExitCommands.size(); i++) {
                                        ImGui.pushID(i);
                                        ImString exitCommand = new ImString(radialRegionData.regionExitCommands.get(i), 256);
                                        if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                            radialRegionData.regionExitCommands.set(i, exitCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            radialRegionData.regionExitCommands.remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }

                                    if (ImGui.button("Add Exit Command")) {
                                        radialRegionData.regionExitCommands.add("");
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound: "); ImGui.sameLine();
                                    if (ImGui.inputText("##RegionSound", imRegionSound)) {
                                        radialRegionData.regionSound = imRegionSound.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imRegionSoundVolume)) {
                                        radialRegionData.regionSoundVolume = imRegionSoundVolume.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Fade Duration: ", imRegionSoundFadeDuration)) {
                                        radialRegionData.regionSoundFadeDuration = imRegionSoundFadeDuration.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.separator();
                                    if (ImGui.button("Delete Region")) {
                                        regionToDelete = radialRegionData; // Save the region data that needs to be deleted to safely delete it outside the loop
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
                    if (ImGui.beginTabItem("Disabled")) {
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
                            RadialRegionData regionToDelete = null;

                            for (RadialRegionData radialRegionData : RegionsManager.activeRegions) {
                                imRegionName.set(radialRegionData.regionName);
                                imRegionX.set(radialRegionData.regionX);
                                imRegionY.set(radialRegionData.regionY);
                                imRegionZ.set(radialRegionData.regionZ);
                                imRegionRadius.set(radialRegionData.regionRadius);
                                imRegionSoundVolume.set(radialRegionData.regionSoundVolume);
                                imRegionSoundFadeDuration.set(radialRegionData.regionSoundFadeDuration);

                                if (!radialRegionData.regionEnabled) {
                                    ImGui.pushID(index);
                                    if (ImGui.collapsingHeader(radialRegionData.regionName + "###region_" + index)) {

                                        ImGui.text("Region Name: "); ImGui.sameLine();
                                        if (ImGui.inputText("##Region Name: ", imRegionName)) {
                                            radialRegionData.regionName = imRegionName.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Enabled: "); ImGui.sameLine();
                                        if (ImGui.checkbox("##Region Enabled: ", radialRegionData.regionEnabled)) {
                                            radialRegionData.regionEnabled = !radialRegionData.regionEnabled;
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region X: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region X: ", imRegionX)) {
                                            radialRegionData.regionX = imRegionX.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Y: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Y: ", imRegionY)) {
                                            radialRegionData.regionY = imRegionY.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Z: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Z: ", imRegionZ)) {
                                            radialRegionData.regionZ = imRegionZ.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        if (ImGui.button("Center Region On Self")) {
                                            ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                            if (player != null) {
                                                Vec3d playerPos = player.getPos();
                                                radialRegionData.regionX = (int) playerPos.getX();
                                                radialRegionData.regionY = (int) playerPos.getY();
                                                radialRegionData.regionZ = (int) playerPos.getZ();
                                                shouldSyncNetwork = true;
                                            }
                                        }

                                        // Region Radius
                                        ImGui.text("Region Radius: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Radius: ", imRegionRadius)) {
                                            radialRegionData.regionRadius = imRegionRadius.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        // Enter Commands
                                        ImGui.text("Region Enter Commands: ");
                                        for (int i = 0; i < radialRegionData.regionEnterCommands.size(); i++) {
                                            ImGui.pushID(i);
                                            ImString enterCommand = new ImString(radialRegionData.regionEnterCommands.get(i), 256);
                                            if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                                radialRegionData.regionEnterCommands.set(i, enterCommand.get());
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.sameLine();
                                            if (ImGui.button("X")) {
                                                radialRegionData.regionEnterCommands.remove(i);
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.popID();
                                        }
                                        if (ImGui.button("Add Enter Command")) {
                                            radialRegionData.regionEnterCommands.add("");
                                            shouldSyncNetwork = true;
                                        }
                                        // Exit Commands
                                        ImGui.text("Region Exit Commands: ");
                                        for (int i = 0; i < radialRegionData.regionExitCommands.size(); i++) {
                                            ImGui.pushID(i);
                                            ImString exitCommand = new ImString(radialRegionData.regionExitCommands.get(i), 256);
                                            if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                                radialRegionData.regionExitCommands.set(i, exitCommand.get());
                                            }
                                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.sameLine();
                                            if (ImGui.button("X")) {
                                                radialRegionData.regionExitCommands.remove(i);
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.popID();
                                        }
                                        if (ImGui.button("Add Exit Command")) {
                                            radialRegionData.regionExitCommands.add("");
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound: "); ImGui.sameLine();
                                        if (ImGui.inputText("##RegionSound", imRegionSound)) {
                                            radialRegionData.regionSound = imRegionSound.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                        if (ImGui.inputFloat("##Region Sound Volume: ", imRegionSoundVolume)) {
                                            radialRegionData.regionSoundVolume = imRegionSoundVolume.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                        if (ImGui.inputFloat("##Region Sound Volume: ", imRegionSoundFadeDuration)) {
                                            radialRegionData.regionSoundFadeDuration = imRegionSoundFadeDuration.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.separator();
                                        if (ImGui.button("Delete Region")) {
                                            // Save the region data that needs to be deleted to safely delete it outside the loop
                                            regionToDelete = radialRegionData;
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
                    ImGui.endTabBar();
                }
                ImGui.endTabItem();
            }

            // Linear Regions Tab
            // Features:
            //  - Regions With A Beginning And End Point, The Area Between Them Creating The Effect Area
            if (ImGui.beginTabItem("Linear Regions")) {
                if (ImGui.beginTabBar("Linear Regions Tab Bar")) {
                    if (ImGui.beginTabItem("Active")) {


                        ImGui.endTabItem();
                    }
                    if (ImGui.beginTabItem("Disabled")) {


                        ImGui.endTabItem();
                    }
                    ImGui.endTabBar();
                }
                ImGui.endTabItem();
            }

            // Mass Region Editing Tab
            // Features:
            //  - Enable All Regions
            //  - Disable All Regions
            //  - Delete All Regions
            if (ImGui.beginTabItem("Mass Region Editing")) {
                if (ImGui.button("Enable All Regions")) {
                    for (RadialRegionData radialRegionData : RegionsManager.activeRegions) {
                        radialRegionData.regionEnabled = true;
                    }
                    shouldSyncNetwork = true;
                }
                if (ImGui.button("Disable All Regions")) {
                    for (RadialRegionData radialRegionData : RegionsManager.activeRegions) {
                        radialRegionData.regionEnabled = false;
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