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
                            imRadialRegionName.set(radialRegionData.getRegionName());
                            imRadialRegionX.set(radialRegionData.getRegionX());
                            imRadialRegionY.set(radialRegionData.getRegionY());
                            imRadialRegionZ.set(radialRegionData.getRegionZ());
                            imRadialRegionRadius.set(radialRegionData.getRegionRadius());
                            imRadialRegionSound.set(radialRegionData.getRegionSound());
                            imRadialRegionSoundVolume.set(radialRegionData.getRegionSoundVolume());
                            imRadialRegionSoundFadeDuration.set(radialRegionData.getRegionSoundFadeDuration());

                            if (radialRegionData.isRegionEnabled()) {
                                ImGui.pushID(index);
                                if (ImGui.collapsingHeader(radialRegionData.getRegionName() + "###region_" + radialRegionData.getRegionUuid())) {

                                    ImGui.text("Region Name: "); ImGui.sameLine();
                                    if (ImGui.inputText("##Region Name: ", imRadialRegionName)) {
                                        radialRegionData.setRegionName(imRadialRegionName.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Enabled: "); ImGui.sameLine();
                                    if (ImGui.checkbox("##Region Enabled: ", radialRegionData.isRegionEnabled())) {
                                        radialRegionData.setRegionEnabled(!radialRegionData.isRegionEnabled());
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region X: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X: ", imRadialRegionX)) {
                                        radialRegionData.setRegionX(imRadialRegionX.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y: ", imRadialRegionY)) {
                                        radialRegionData.setRegionY(imRadialRegionY.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z: ", imRadialRegionZ)) {
                                        radialRegionData.setRegionZ(imRadialRegionZ.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            radialRegionData.setRegionPos((int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ());
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    ImGui.text("Region Radius: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Radius: ", imRadialRegionRadius)) {
                                        radialRegionData.setRegionRadius(imRadialRegionRadius.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    // Enter Commands
                                    ImGui.text("Region Enter Commands: ");
                                    for (int i = 0; i < radialRegionData.getRegionEnterCommands().size(); i++) {
                                        ImGui.pushID(i);
                                        ImString enterCommand = new ImString(radialRegionData.getRegionEnterCommands().get(i), 256);
                                        if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                            radialRegionData.getRegionEnterCommands().set(i, enterCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            radialRegionData.getRegionEnterCommands().remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }

                                    if (ImGui.button("Add Enter Command")) {
                                        radialRegionData.getRegionEnterCommands().add("");
                                        shouldSyncNetwork = true;
                                    }

                                    // Exit Commands
                                    ImGui.text("Region Exit Commands: ");
                                    for (int i = 0; i < radialRegionData.getRegionExitCommands().size(); i++) {
                                        ImGui.pushID(i);
                                        ImString exitCommand = new ImString(radialRegionData.getRegionExitCommands().get(i), 256);
                                        if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                            radialRegionData.getRegionExitCommands().set(i, exitCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            radialRegionData.getRegionExitCommands().remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }

                                    if (ImGui.button("Add Exit Command")) {
                                        radialRegionData.getRegionExitCommands().add("");
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound: "); ImGui.sameLine();
                                    if (ImGui.inputText("##RegionSound", imRadialRegionSound)) {
                                        radialRegionData.setRegionSound(imRadialRegionSound.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imRadialRegionSoundVolume)) {
                                        radialRegionData.setRegionSoundVolume(imRadialRegionSoundVolume.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Fade Duration: ", imRadialRegionSoundFadeDuration)) {
                                        radialRegionData.setRegionSoundFadeDuration(imRadialRegionSoundFadeDuration.get());
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
                                imRadialRegionName.set(radialRegionData.getRegionName());
                                imRadialRegionX.set(radialRegionData.getRegionX());
                                imRadialRegionY.set(radialRegionData.getRegionY());
                                imRadialRegionZ.set(radialRegionData.getRegionZ());
                                imRadialRegionRadius.set(radialRegionData.getRegionRadius());
                                imRadialRegionSoundVolume.set(radialRegionData.getRegionSoundVolume());
                                imRadialRegionSoundFadeDuration.set(radialRegionData.getRegionSoundFadeDuration());

                                if (!radialRegionData.isRegionEnabled()) {
                                    ImGui.pushID(index);
                                    if (ImGui.collapsingHeader(radialRegionData.getRegionName() + "###region_" + index)) {

                                        ImGui.text("Region Name: "); ImGui.sameLine();
                                        if (ImGui.inputText("##Region Name: ", imRadialRegionName)) {
                                            radialRegionData.setRegionName(imRadialRegionName.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Enabled: "); ImGui.sameLine();
                                        if (ImGui.checkbox("##Region Enabled: ", radialRegionData.isRegionEnabled())) {
                                            radialRegionData.setRegionEnabled(!radialRegionData.isRegionEnabled());
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region X: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region X: ", imRadialRegionX)) {
                                            radialRegionData.setRegionX(imRadialRegionX.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Y: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Y: ", imRadialRegionY)) {
                                            radialRegionData.setRegionY(imRadialRegionY.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Z: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Z: ", imRadialRegionZ)) {
                                            radialRegionData.setRegionZ(imRadialRegionZ.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        if (ImGui.button("Center Region On Self")) {
                                            ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                            if (player != null) {
                                                Vec3d playerPos = player.getPos();
                                                radialRegionData.setRegionPos((int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ());
                                                shouldSyncNetwork = true;
                                            }
                                        }

                                        // Region Radius
                                        ImGui.text("Region Radius: "); ImGui.sameLine();
                                        if (ImGui.inputInt("##Region Radius: ", imRadialRegionRadius)) {
                                            radialRegionData.setRegionRadius(imRadialRegionRadius.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        // Enter Commands
                                        ImGui.text("Region Enter Commands: ");
                                        for (int i = 0; i < radialRegionData.getRegionEnterCommands().size(); i++) {
                                            ImGui.pushID(i);
                                            ImString enterCommand = new ImString(radialRegionData.getRegionEnterCommands().get(i), 256);
                                            if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                                radialRegionData.getRegionEnterCommands().set(i, enterCommand.get());
                                            }
                                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.sameLine();
                                            if (ImGui.button("X")) {
                                                radialRegionData.getRegionEnterCommands().remove(i);
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.popID();
                                        }
                                        if (ImGui.button("Add Enter Command")) {
                                            radialRegionData.getRegionEnterCommands().add("");
                                            shouldSyncNetwork = true;
                                        }
                                        // Exit Commands
                                        ImGui.text("Region Exit Commands: ");
                                        for (int i = 0; i < radialRegionData.getRegionExitCommands().size(); i++) {
                                            ImGui.pushID(i);
                                            ImString exitCommand = new ImString(radialRegionData.getRegionExitCommands().get(i), 256);
                                            if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                                radialRegionData.getRegionExitCommands().set(i, exitCommand.get());
                                            }
                                            if (ImGui.isItemDeactivatedAfterEdit()) {
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.sameLine();
                                            if (ImGui.button("X")) {
                                                radialRegionData.getRegionExitCommands().remove(i);
                                                shouldSyncNetwork = true;
                                            }
                                            ImGui.popID();
                                        }
                                        if (ImGui.button("Add Exit Command")) {
                                            radialRegionData.getRegionExitCommands().add("");
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound: "); ImGui.sameLine();
                                        if (ImGui.inputText("##RegionSound", imRadialRegionSound)) {
                                            radialRegionData.setRegionSound(imRadialRegionSound.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                        if (ImGui.inputFloat("##Region Sound Volume: ", imRadialRegionSoundVolume)) {
                                            radialRegionData.setRegionSoundVolume(imRadialRegionSoundVolume.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }

                                        ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                        if (ImGui.inputFloat("##Region Sound Volume: ", imRadialRegionSoundFadeDuration)) {
                                            radialRegionData.setRegionSoundFadeDuration(imRadialRegionSoundFadeDuration.get());
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
                            imLinearRegionName.set(linearRegionData.getRegionName());
                            imLinearRegionX1.set(linearRegionData.getRegionX1());
                            imLinearRegionY1.set(linearRegionData.getRegionY1());
                            imLinearRegionZ1.set(linearRegionData.getRegionZ1());
                            imLinearRegionX2.set(linearRegionData.getRegionX2());
                            imLinearRegionY2.set(linearRegionData.getRegionY2());
                            imLinearRegionZ2.set(linearRegionData.getRegionZ2());
                            imLinearRegionSound.set(linearRegionData.getRegionSound());
                            imLinearRegionSoundVolume.set(linearRegionData.getRegionSoundVolume());
                            imLinearRegionSoundFadeDuration.set(linearRegionData.getRegionSoundFadeDuration());

                            if (linearRegionData.isRegionEnabled()) {
                                ImGui.pushID(index);
                                if (ImGui.collapsingHeader(linearRegionData.getRegionName() + "###region_" + index)) {

                                    ImGui.text("Region Name: "); ImGui.sameLine();
                                    if (ImGui.inputText("##Region Name: ", imLinearRegionName)) {
                                        linearRegionData.setRegionName(imLinearRegionName.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Enabled: "); ImGui.sameLine();
                                    if (ImGui.checkbox("##Region Enabled: ", linearRegionData.isRegionEnabled())) {
                                        linearRegionData.setRegionEnabled(!linearRegionData.isRegionEnabled());
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Position 1");
                                    ImGui.text("Region X1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X1: ", imLinearRegionX1)) {
                                        linearRegionData.setRegionX1(imLinearRegionX1.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y1: ", imLinearRegionY1)) {
                                        linearRegionData.setRegionY1(imLinearRegionY1.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z1: ", imLinearRegionZ1)) {
                                        linearRegionData.setRegionZ1(imLinearRegionZ1.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos1 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.setRegionPos1((int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ());
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    ImGui.text("Region Position 2");
                                    ImGui.text("Region X2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X2: ", imLinearRegionX2)) {
                                        linearRegionData.setRegionX2(imLinearRegionX2.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y2: ", imLinearRegionY2)) {
                                        linearRegionData.setRegionY2(imLinearRegionY2.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z2: ", imLinearRegionZ2)) {
                                        linearRegionData.setRegionZ2(imLinearRegionZ2.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos2 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.setRegionPos2((int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ());
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    // Enter Commands
                                    ImGui.text("Region Enter Commands: ");
                                    for (int i = 0; i < linearRegionData.getRegionEnterCommands().size(); i++) {
                                        ImGui.pushID(i);
                                        ImString enterCommand = new ImString(linearRegionData.getRegionEnterCommands().get(i), 256);
                                        if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                            linearRegionData.getRegionEnterCommands().set(i, enterCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.getRegionEnterCommands().remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Enter Command")) {
                                        linearRegionData.getRegionEnterCommands().add("");
                                        shouldSyncNetwork = true;
                                    }
                                    // Exit Commands
                                    ImGui.text("Region Exit Commands: ");
                                    for (int i = 0; i < linearRegionData.getRegionExitCommands().size(); i++) {
                                        ImGui.pushID(i);
                                        ImString exitCommand = new ImString(linearRegionData.getRegionExitCommands().get(i), 256);
                                        if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                            linearRegionData.getRegionExitCommands().set(i, exitCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.getRegionExitCommands().remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Exit Command")) {
                                        linearRegionData.getRegionExitCommands().add("");
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound: "); ImGui.sameLine();
                                    if (ImGui.inputText("##RegionSound", imLinearRegionSound)) {
                                        linearRegionData.setRegionSound(imLinearRegionSound.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundVolume)) {
                                        linearRegionData.setRegionSoundVolume(imLinearRegionSoundVolume.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundFadeDuration)) {
                                        linearRegionData.setRegionSoundFadeDuration(imLinearRegionSoundFadeDuration.get());
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
                            imLinearRegionName.set(linearRegionData.getRegionName());
                            imLinearRegionX1.set(linearRegionData.getRegionX1());
                            imLinearRegionY1.set(linearRegionData.getRegionY1());
                            imLinearRegionZ1.set(linearRegionData.getRegionZ1());
                            imLinearRegionX2.set(linearRegionData.getRegionX2());
                            imLinearRegionY2.set(linearRegionData.getRegionY2());
                            imLinearRegionZ2.set(linearRegionData.getRegionZ2());
                            imLinearRegionSound.set(linearRegionData.getRegionSound());
                            imLinearRegionSoundVolume.set(linearRegionData.getRegionSoundVolume());
                            imLinearRegionSoundFadeDuration.set(linearRegionData.getRegionSoundFadeDuration());

                            if (!linearRegionData.isRegionEnabled()) {
                                ImGui.pushID(index);
                                if (ImGui.collapsingHeader(linearRegionData.getRegionName() + "###region_" + index)) {

                                    ImGui.text("Region Name: "); ImGui.sameLine();
                                    if (ImGui.inputText("##Region Name: ", imLinearRegionName)) {
                                        linearRegionData.setRegionName(imLinearRegionName.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Enabled: "); ImGui.sameLine();
                                    if (ImGui.checkbox("##Region Enabled: ", linearRegionData.isRegionEnabled())) {
                                        linearRegionData.setRegionEnabled(!linearRegionData.isRegionEnabled());
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Position 1");
                                    ImGui.text("Region X1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X1: ", imLinearRegionX1)) {
                                        linearRegionData.setRegionX1(imLinearRegionX1.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y1: ", imLinearRegionY1)) {
                                        linearRegionData.setRegionY1(imLinearRegionY1.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z1: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z1: ", imLinearRegionZ1)) {
                                        linearRegionData.setRegionZ1(imLinearRegionZ1.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos1 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.setRegionPos1((int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ());
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    ImGui.text("Region Position 2");
                                    ImGui.text("Region X2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region X2: ", imLinearRegionX2)) {
                                        linearRegionData.setRegionX2(imLinearRegionX2.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Y2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Y2: ", imLinearRegionY2)) {
                                        linearRegionData.setRegionY2(imLinearRegionY2.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Z2: "); ImGui.sameLine();
                                    if (ImGui.inputInt("##Region Z2: ", imLinearRegionZ2)) {
                                        linearRegionData.setRegionZ2(imLinearRegionZ2.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    if (ImGui.button("Center Region Pos2 On Self")) {
                                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                                        if (player != null) {
                                            Vec3d playerPos = player.getPos();
                                            linearRegionData.setRegionPos2((int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ());
                                            shouldSyncNetwork = true;
                                        }
                                    }

                                    // Enter Commands
                                    ImGui.text("Region Enter Commands: ");
                                    for (int i = 0; i < linearRegionData.getRegionEnterCommands().size(); i++) {
                                        ImGui.pushID(i);
                                        ImString enterCommand = new ImString(linearRegionData.getRegionEnterCommands().get(i), 256);
                                        if (ImGui.inputText("##EnterCommand", enterCommand)) {
                                            linearRegionData.getRegionEnterCommands().set(i, enterCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.getRegionEnterCommands().remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Enter Command")) {
                                        linearRegionData.getRegionEnterCommands().add("");
                                        shouldSyncNetwork = true;
                                    }
                                    // Exit Commands
                                    ImGui.text("Region Exit Commands: ");
                                    for (int i = 0; i < linearRegionData.getRegionExitCommands().size(); i++) {
                                        ImGui.pushID(i);
                                        ImString exitCommand = new ImString(linearRegionData.getRegionExitCommands().get(i), 256);
                                        if (ImGui.inputText("##ExitCommand", exitCommand)) {
                                            linearRegionData.getRegionExitCommands().set(i, exitCommand.get());
                                        }
                                        if (ImGui.isItemDeactivatedAfterEdit()) {
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.sameLine();
                                        if (ImGui.button("X")) {
                                            linearRegionData.getRegionExitCommands().remove(i);
                                            shouldSyncNetwork = true;
                                        }
                                        ImGui.popID();
                                    }
                                    if (ImGui.button("Add Exit Command")) {
                                        linearRegionData.getRegionExitCommands().add("");
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound: "); ImGui.sameLine();
                                    if (ImGui.inputText("##RegionSound", imLinearRegionSound)) {
                                        linearRegionData.setRegionSound(imLinearRegionSound.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Volume: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundVolume)) {
                                        linearRegionData.setRegionSoundVolume(imLinearRegionSoundVolume.get());
                                    }
                                    if (ImGui.isItemDeactivatedAfterEdit()) {
                                        shouldSyncNetwork = true;
                                    }

                                    ImGui.text("Region Sound Fade Duration: "); ImGui.sameLine();
                                    if (ImGui.inputFloat("##Region Sound Volume: ", imLinearRegionSoundFadeDuration)) {
                                        linearRegionData.setRegionSoundFadeDuration(imLinearRegionSoundFadeDuration.get());
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
                        radialRegionData.setRegionEnabled(true);
                    }
                    for (LinearRegionData linearRegionData : RegionsManager.activeLinearRegions) {
                        linearRegionData.setRegionEnabled(true);
                    }
                    shouldSyncNetwork = true;
                }
                if (ImGui.button("Disable All Regions")) {
                    for (RadialRegionData radialRegionData : RegionsManager.activeRadialRegions) {
                        radialRegionData.setRegionEnabled(false);
                    }
                    for (LinearRegionData linearRegionData : RegionsManager.activeLinearRegions) {
                        linearRegionData.setRegionEnabled(false);
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