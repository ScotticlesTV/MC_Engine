package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.networking.regions.payloads.sendRegionsDataPayload;
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

    // Track if any text inputs, sliders, or checkboxes were interacted with this frame
    private static boolean shouldSyncNetwork = false;

    public static void showRegionsEditorUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) return;

            shouldSyncNetwork = false; // Reset status at start of frame
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
                                20, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), true);
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

                    if (regionData.regionEnabled) {
                        ImGui.pushID(index);
                        if (ImGui.collapsingHeader(regionData.regionName + "###region_" + index)) {

                            ImGui.text("Region Name: "); ImGui.sameLine();
                            if (ImGui.inputText("##Region Name: ", imRegionName)) {
                                regionData.regionName = imRegionName.get();
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
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Y: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Y: ", imRegionY)) {
                                regionData.regionY = imRegionY.get();
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Z: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Z: ", imRegionZ)) {
                                regionData.regionZ = imRegionZ.get();
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
                                20, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), false);
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

                    if (!regionData.regionEnabled) {
                        ImGui.pushID(index);
                        if (ImGui.collapsingHeader(regionData.regionName + "###region_" + index)) {

                            ImGui.text("Region Name: "); ImGui.sameLine();
                            if (ImGui.inputText("##Region Name: ", imRegionName)) {
                                regionData.regionName = imRegionName.get();
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
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Y: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Y: ", imRegionY)) {
                                regionData.regionY = imRegionY.get();
                                shouldSyncNetwork = true;
                            }

                            ImGui.text("Region Z: "); ImGui.sameLine();
                            if (ImGui.inputInt("##Region Z: ", imRegionZ)) {
                                regionData.regionZ = imRegionZ.get();
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
            ImGui.endTabBar();
            ImGui.end();

            // Server/Network syncing: Send the client's up to date set of regions to the server
            if (shouldSyncNetwork) {
                ClientPlayNetworking.send(new sendRegionsDataPayload(new HashSet<>(RegionsManager.activeRegions)));
            }
        }
    }
}