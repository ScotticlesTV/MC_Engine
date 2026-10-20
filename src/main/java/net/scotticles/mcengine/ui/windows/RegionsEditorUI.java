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
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRadialRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;
import net.scotticles.mcengine.ui.UIManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class RegionsEditorUI {
    static ImString imRadialRegionName = new ImString("Region", 256);
    static ImInt imRadialRegionX = new ImInt(0);
    static ImInt imRadialRegionY = new ImInt(0);
    static ImInt imRadialRegionZ = new ImInt(0);
    static ImInt imRadialRegionRadius = new ImInt(0);
    static ImString imRadialRegionSound = new ImString("", 256);
    static ImFloat imRadialRegionSoundVolume = new ImFloat(1.0f);
    static ImFloat imRadialRegionSoundFadeDuration = new ImFloat(2.0f);

    static ImString imLinearRegionName = new ImString("Region", 256);
    static ImInt imLinearRegionX1 = new ImInt(0);
    static ImInt imLinearRegionY1 = new ImInt(0);
    static ImInt imLinearRegionZ1 = new ImInt(0);
    static ImInt imLinearRegionX2 = new ImInt(0);
    static ImInt imLinearRegionY2 = new ImInt(0);
    static ImInt imLinearRegionZ2 = new ImInt(0);
    static ImString imLinearRegionSound = new ImString("", 256);
    static ImFloat imLinearRegionSoundVolume = new ImFloat(1.0f);
    static ImFloat imLinearRegionSoundFadeDuration = new ImFloat(2.0f);

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
                            // DONE(priority:medium category:Clarity @Scott): Change region default name to "Region" + "RegionUUID"
                            // DONE(priority:high category:Cleanup @Scott): Move as much logic of Add Region as possible to RegionsManager to make it easier to find
                            ClientPlayerEntity player = MinecraftClient.getInstance().player;
                            if (player != null) {
                                Vec3d playerPos = player.getPos();
                                RegionsManager.addRadialRegion(playerPos);
                                // Trigger Sync
                                shouldSyncNetwork = true;
                            }
                        }

                        int index = 0;
                        RadialRegionData regionToDelete = null;

                        // Loop Through Each Radial Region Data Object
                        for (RadialRegionData radialRegionData : RegionsManager.activeRadialRegions) {
                            imRadialRegionName.set(radialRegionData.regionName);
                            imRadialRegionX.set(radialRegionData.regionX);
                            imRadialRegionY.set(radialRegionData.regionY);
                            imRadialRegionZ.set(radialRegionData.regionZ);
                            imRadialRegionRadius.set(radialRegionData.regionRadius);
                            imRadialRegionSound.set(radialRegionData.regionSound);
                            imRadialRegionSoundVolume.set(radialRegionData.regionSoundVolume);
                            imRadialRegionSoundFadeDuration.set(radialRegionData.regionSoundFadeDuration);

                            if (radialRegionData.regionEnabled) {
                                ImGui.pushID(index);
                                if (ImGui.collapsingHeader(radialRegionData.regionName + "###region_" + radialRegionData.regionUuid)) {

                                    ImGui.text("Region Name: "); ImGui.sameLine();
                                    if (ImGui.inputText("##Region Name: ", imRadialRegionName)) {
                                        radialRegionData.regionName = imRadialRegionName.get();
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
                                    if (ImGui.inputInt("##Region X: ", imRadialRegionX)) {
                                        radialRegionData.regionX = imRadialRegionX.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y: ", imRadialRegionY)) {
                                        radialRegionData.regionY = imRadialRegionY.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z: ", imRadialRegionZ)) {
                                        radialRegionData.regionZ = imRadialRegionZ.get();
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
                                    if (ImGui.inputInt("##Region Radius: ", imRadialRegionRadius)) {
                                        radialRegionData.regionRadius = imRadialRegionRadius.get();
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
                                    if (ImGui.inputText("##RegionSound", imRadialRegionSound)) {
                                        radialRegionData.regionSound = imRadialRegionSound.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imRadialRegionSoundVolume)) {
                                        radialRegionData.regionSoundVolume = imRadialRegionSoundVolume.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Fade Duration: ", imRadialRegionSoundFadeDuration)) {
                                        radialRegionData.regionSoundFadeDuration = imRadialRegionSoundFadeDuration.get();
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
                            RegionsManager.activeRadialRegions.remove(regionToDelete);
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
                                    RegionsManager.addRadialRegion(playerPos);
                                    shouldSyncNetwork = true; // Trigger Sync
                                }
                            }

                            int index = 0;
                            RadialRegionData regionToDelete = null;

                            for (RadialRegionData radialRegionData : RegionsManager.activeRadialRegions) {
                                imRadialRegionName.set(radialRegionData.regionName);
                                imRadialRegionX.set(radialRegionData.regionX);
                                imRadialRegionY.set(radialRegionData.regionY);
                                imRadialRegionZ.set(radialRegionData.regionZ);
                                imRadialRegionRadius.set(radialRegionData.regionRadius);
                                imRadialRegionSoundVolume.set(radialRegionData.regionSoundVolume);
                                imRadialRegionSoundFadeDuration.set(radialRegionData.regionSoundFadeDuration);

                                if (!radialRegionData.regionEnabled) {
                                    ImGui.pushID(index);
                                    if (ImGui.collapsingHeader(radialRegionData.regionName + "###region_" + index)) {

                                        ImGui.text("Region Name: "); ImGui.sameLine();
                                        if (ImGui.inputText("##Region Name: ", imRadialRegionName)) {
                                            radialRegionData.regionName = imRadialRegionName.get();
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
                                        if (ImGui.inputInt("##Region X: ", imRadialRegionX)) {
                                            radialRegionData.regionX = imRadialRegionX.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Y: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Y: ", imRadialRegionY)) {
                                            radialRegionData.regionY = imRadialRegionY.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Z: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Z: ", imRadialRegionZ)) {
                                            radialRegionData.regionZ = imRadialRegionZ.get();
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
                                        if (ImGui.inputInt("##Region Radius: ", imRadialRegionRadius)) {
                                            radialRegionData.regionRadius = imRadialRegionRadius.get();
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
                                        if (ImGui.inputText("##RegionSound", imRadialRegionSound)) {
                                            radialRegionData.regionSound = imRadialRegionSound.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                        if (ImGui.inputFloat("##Region Sound Volume: ", imRadialRegionSoundVolume)) {
                                            radialRegionData.regionSoundVolume = imRadialRegionSoundVolume.get();
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                        if (ImGui.inputFloat("##Region Sound Volume: ", imRadialRegionSoundFadeDuration)) {
                                            radialRegionData.regionSoundFadeDuration = imRadialRegionSoundFadeDuration.get();
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
                                RegionsManager.activeRadialRegions.remove(regionToDelete);
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
                        if (ImGui.button("Add Active Region")) {
                            ClientPlayerEntity player = MinecraftClient.getInstance().player;
                            if (player != null) {
                                Vec3d playerPos = player.getPos();
                                RegionsManager.addLinearRegion(playerPos);
                                // Trigger Sync
                                shouldSyncNetwork = true;
                            }
                        }

                        int index = 0;
                        LinearRegionData regionToDelete = null;

                        for (LinearRegionData linearRegionData : RegionsManager.activeLinearRegions) {
                            imLinearRegionName.set(linearRegionData.regionName);
                            imLinearRegionX1.set(linearRegionData.regionX1);
                            imLinearRegionY1.set(linearRegionData.regionY1);
                            imLinearRegionZ1.set(linearRegionData.regionZ1);
                            imLinearRegionX2.set(linearRegionData.regionX2);
                            imLinearRegionY2.set(linearRegionData.regionY2);
                            imLinearRegionZ2.set(linearRegionData.regionZ2);
                            imLinearRegionSound.set(linearRegionData.regionSound);
                            imLinearRegionSoundVolume.set(linearRegionData.regionSoundVolume);
                            imLinearRegionSoundFadeDuration.set(linearRegionData.regionSoundFadeDuration);

                            if (linearRegionData.regionEnabled) {
                                ImGui.pushID(index);
                                if (ImGui.collapsingHeader(linearRegionData.regionName + "###region_" + index)) {

                                    ImGui.text("Region Name: "); ImGui.sameLine();
                                    if (ImGui.inputText("##Region Name: ", imLinearRegionName)) {
                                        linearRegionData.regionName = imLinearRegionName.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Enabled: "); ImGui.sameLine();
                                    if (ImGui.checkbox("##Region Enabled: ", linearRegionData.regionEnabled)) {
                                        linearRegionData.regionEnabled = !linearRegionData.regionEnabled;
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Position 1");
                                    ImGui.text("Region X1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X1: ", imLinearRegionX1)) {
                                        linearRegionData.regionX1 = imLinearRegionX1.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y1: ", imLinearRegionY1)) {
                                        linearRegionData.regionY1 = imLinearRegionY1.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z1: ", imLinearRegionZ1)) {
                                        linearRegionData.regionZ1 = imLinearRegionZ1.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos1 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.regionX1 = (int) playerPos.getX();
                                            linearRegionData.regionY1 = (int) playerPos.getY();
                                            linearRegionData.regionZ1 = (int) playerPos.getZ();
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    ImGui.text("Region Position 2");
                                    ImGui.text("Region X2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X2: ", imLinearRegionX2)) {
                                        linearRegionData.regionX2 = imLinearRegionX2.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y2: ", imLinearRegionY2)) {
                                        linearRegionData.regionY2 = imLinearRegionY2.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z2: ", imLinearRegionZ2)) {
                                        linearRegionData.regionZ2 = imLinearRegionZ2.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos2 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.regionX2 = (int) playerPos.getX();
                                            linearRegionData.regionY2 = (int) playerPos.getY();
                                            linearRegionData.regionZ2 = (int) playerPos.getZ();
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    // Enter Commands
                                    ImGui.text("Region Enter Commands: ");
                                    for (int i = 0; i < linearRegionData.regionEnterCommands.size(); i++) {
                                        ImGui.pushID(i);
                                        ImString enterCommand = new ImString(linearRegionData.regionEnterCommands.get(i), 256);
                                        if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                            linearRegionData.regionEnterCommands.set(i, enterCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.regionEnterCommands.remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Enter Command")) {
                                        linearRegionData.regionEnterCommands.add("");
                                        shouldSyncNetwork = true;
                                    }
                                    // Exit Commands
                                    ImGui.text("Region Exit Commands: ");
                                    for (int i = 0; i < linearRegionData.regionExitCommands.size(); i++) {
                                        ImGui.pushID(i);
                                        ImString exitCommand = new ImString(linearRegionData.regionExitCommands.get(i), 256);
                                        if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                            linearRegionData.regionExitCommands.set(i, exitCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.regionExitCommands.remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Exit Command")) {
                                        linearRegionData.regionExitCommands.add("");
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound: "); ImGui.sameLine();
                                    if (ImGui.inputText("##RegionSound", imLinearRegionSound)) {
                                        linearRegionData.regionSound = imLinearRegionSound.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundVolume)) {
                                        linearRegionData.regionSoundVolume = imLinearRegionSoundVolume.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundFadeDuration)) {
                                        linearRegionData.regionSoundFadeDuration = imLinearRegionSoundFadeDuration.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.separator();
                                    if (ImGui.button("Delete Region")) {
                                        // Save the region data that needs to be deleted to safely delete it outside the loop
                                        regionToDelete = linearRegionData;
                                    }
                                }
                                ImGui.popID();
                                index++;
                            }
                        }

                        if (regionToDelete != null) {
                            RegionsManager.activeLinearRegions.remove(regionToDelete);
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
                                RegionsManager.addLinearRegion(playerPos);
                                shouldSyncNetwork = true; // Trigger Sync
                            }
                        }

                        int index = 0;
                        LinearRegionData regionToDelete = null;

                        for (LinearRegionData linearRegionData : RegionsManager.activeLinearRegions) {
                            imLinearRegionName.set(linearRegionData.regionName);
                            imLinearRegionX1.set(linearRegionData.regionX1);
                            imLinearRegionY1.set(linearRegionData.regionY1);
                            imLinearRegionZ1.set(linearRegionData.regionZ1);
                            imLinearRegionX2.set(linearRegionData.regionX2);
                            imLinearRegionY2.set(linearRegionData.regionY2);
                            imLinearRegionZ2.set(linearRegionData.regionZ2);
                            imLinearRegionSound.set(linearRegionData.regionSound);
                            imLinearRegionSoundVolume.set(linearRegionData.regionSoundVolume);
                            imLinearRegionSoundFadeDuration.set(linearRegionData.regionSoundFadeDuration);

                            if (!linearRegionData.regionEnabled) {
                                ImGui.pushID(index);
                                if (ImGui.collapsingHeader(linearRegionData.regionName + "###region_" + index)) {

                                    ImGui.text("Region Name: "); ImGui.sameLine();
                                    if (ImGui.inputText("##Region Name: ", imLinearRegionName)) {
                                        linearRegionData.regionName = imLinearRegionName.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Enabled: "); ImGui.sameLine();
                                    if (ImGui.checkbox("##Region Enabled: ", linearRegionData.regionEnabled)) {
                                        linearRegionData.regionEnabled = !linearRegionData.regionEnabled;
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Position 1");
                                    ImGui.text("Region X1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X1: ", imLinearRegionX1)) {
                                        linearRegionData.regionX1 = imLinearRegionX1.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y1: ", imLinearRegionY1)) {
                                        linearRegionData.regionY1 = imLinearRegionY1.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z1: ", imLinearRegionZ1)) {
                                        linearRegionData.regionZ1 = imLinearRegionZ1.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos1 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.regionX1 = (int) playerPos.getX();
                                            linearRegionData.regionY1 = (int) playerPos.getY();
                                            linearRegionData.regionZ1 = (int) playerPos.getZ();
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    ImGui.text("Region Position 2");
                                    ImGui.text("Region X2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X2: ", imLinearRegionX2)) {
                                        linearRegionData.regionX2 = imLinearRegionX2.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y2: ", imLinearRegionY2)) {
                                        linearRegionData.regionY2 = imLinearRegionY2.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z2: ", imLinearRegionZ2)) {
                                        linearRegionData.regionZ2 = imLinearRegionZ2.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos2 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.regionX2 = (int) playerPos.getX();
                                            linearRegionData.regionY2 = (int) playerPos.getY();
                                            linearRegionData.regionZ2 = (int) playerPos.getZ();
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    // Enter Commands
                                    ImGui.text("Region Enter Commands: ");
                                    for (int i = 0; i < linearRegionData.regionEnterCommands.size(); i++) {
                                        ImGui.pushID(i);
                                        ImString enterCommand = new ImString(linearRegionData.regionEnterCommands.get(i), 256);
                                        if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                            linearRegionData.regionEnterCommands.set(i, enterCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.regionEnterCommands.remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Enter Command")) {
                                        linearRegionData.regionEnterCommands.add("");
                                        shouldSyncNetwork = true;
                                    }
                                    // Exit Commands
                                    ImGui.text("Region Exit Commands: ");
                                    for (int i = 0; i < linearRegionData.regionExitCommands.size(); i++) {
                                        ImGui.pushID(i);
                                        ImString exitCommand = new ImString(linearRegionData.regionExitCommands.get(i), 256);
                                        if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                            linearRegionData.regionExitCommands.set(i, exitCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.regionExitCommands.remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Exit Command")) {
                                        linearRegionData.regionExitCommands.add("");
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound: "); ImGui.sameLine();
                                    if (ImGui.inputText("##RegionSound", imLinearRegionSound)) {
                                        linearRegionData.regionSound = imLinearRegionSound.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundVolume)) {
                                        linearRegionData.regionSoundVolume = imLinearRegionSoundVolume.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundFadeDuration)) {
                                        linearRegionData.regionSoundFadeDuration = imLinearRegionSoundFadeDuration.get();
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.separator();
                                    if (ImGui.button("Delete Region")) {
                                        // Save the region data that needs to be deleted to safely delete it outside the loop
                                        regionToDelete = linearRegionData;
                                    }
                                }
                                ImGui.popID();
                                index++;
                            }
                        }

                        if (regionToDelete != null) {
                            RegionsManager.activeLinearRegions.remove(regionToDelete);
                            shouldSyncNetwork = true;
                        }
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
                    for (RadialRegionData radialRegionData : RegionsManager.activeRadialRegions) {
                        radialRegionData.regionEnabled = true;
                    }
                    for (LinearRegionData linearRegionData : RegionsManager.activeLinearRegions) {
                        linearRegionData.regionEnabled = true;
                    }
                    shouldSyncNetwork = true;
                }
                if (ImGui.button("Disable All Regions")) {
                    for (RadialRegionData radialRegionData : RegionsManager.activeRadialRegions) {
                        radialRegionData.regionEnabled = false;
                    }
                    for (LinearRegionData linearRegionData : RegionsManager.activeLinearRegions) {
                        linearRegionData.regionEnabled = false;
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
                ClientPlayNetworking.send(new SyncRadialRegionsDataPayload(new ArrayList<>(RegionsManager.activeRadialRegions)));
                ClientPlayNetworking.send(new SyncLinearRegionsDataPayload(new ArrayList<>(RegionsManager.activeLinearRegions)));
            }
        }
    }
}