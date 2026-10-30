package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.type.ImString;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Util;
import net.scotticles.mcengine.lights.LightProfileManager;
import net.scotticles.mcengine.lights.lightdatasaving.LightDataManager;
import net.scotticles.mcengine.networking.lights.payloads.LoadLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.SaveLightsDataPayload;
import net.scotticles.mcengine.ui.UIManager;

import java.util.ArrayList;

public class LightProfilesUI {
    public static ImString lpName = new ImString("", 256);
    public static ImString newLPName = new ImString("", 256);

    public static void showLightProfilesUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // ImGUI Code
            ImVec2 screenCenter = ImGui.getMainViewport().getCenter();
            ImGui.setNextWindowPos(screenCenter.x, screenCenter.y, ImGuiCond.Once, 0.5f, 0.5f);
//            ImGui.setNextWindowSize(820, 520, ImGuiCond.Once);

            ImGui.begin("Light Profiles", UIManager.showLightsProfilesUI);
            ImGui.textWrapped("Edit the world's light profiles.");
            ImGui.beginTabBar("Light Profiles");
            if (ImGui.beginTabItem("Active Profile")) {
                ImGui.textWrapped("Current Light Profile");
                ImGui.separator();
                // Currently Active LP Name
                ImGui.textWrapped("Name: ");
                ImGui.sameLine();
                ImGui.textWrapped(lpName.get());

//                ImGui.setItemTooltip("Delete The Currently Open Light Profile - This Will Load The First Remaining Profile In The List");
//                ImGui.button("Delete Current Profile");

                ImGui.dummy(0f, 20f);
                ImGui.textWrapped("All Light Profiles");
                ImGui.separator();
                // List of All LP Names
                ImGui.indent();
                for (String s : LightProfileManager.lpNames) {
                    ImGui.bulletText(s);
                }
                ImGui.unindent();

                ImGui.dummy(0f, 20f);
                ImGui.textWrapped("Light Profile Editing");
                ImGui.separator();

                // Text Input - LP Name For Action
                ImGui.inputText("Name", newLPName);
                ImGui.setItemTooltip("Set the Light Profile name to interact with.");

                // Save All Lights To A File Based On The Above LP Name
                if (ImGui.button("Save")) {
                    // Send A Packet To Tell The Server To Save The Current Lights Into A LP File With The Above Specified Name
                    ClientPlayNetworking.send(new SaveLightsDataPayload(newLPName.get()));
                }
                ImGui.setItemTooltip("Save all currently loaded lights to a Light Profile file with the specified name.");

                // Load Lights From A File Based On The Above LP Name
                ImGui.sameLine();
                if (ImGui.button("Load")) {
                    // Send A Packet To Tell The Server To Load Loads From A LP File With The Above Specified Name
                    ClientPlayNetworking.send(new LoadLightsDataPayload(newLPName.get()));
                }
                ImGui.setItemTooltip("Load lights from a Light Profile file with the specified name.");

                // Make A New Light Profile File Based On The Above LP name
                ImGui.sameLine();
                if (ImGui.button("New")) {
//                    ClientPlayNetworking.send(new LoadLightsDataPayload(newLPName.get()));
                }
                ImGui.setItemTooltip("Make a new Light Profile file with the specified name.");

                // Delete A Light Profile File Based On The Above LP name
                ImGui.sameLine();
                if (ImGui.button("Delete")) {
                }
                ImGui.setItemTooltip("Delete a Light Profile file with the specified name.");
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Mass Light Profile Editing")) {
                // Delete All Light Profiles
                if (ImGui.button("Clear All Light Profiles")) {
                    System.out.println("Clearing all Light Profiles");
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();

            ImGui.end();
        }
    }
}