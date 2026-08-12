package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.ui.UIManager;

public class WorldGamerulesUI {

    public static boolean doDaylightCycle = false;
    public static boolean doWeatherCycle = false;
    public static boolean sendCommandFeedback = false;
    public static boolean commandBlockOutput = false;
    public static boolean doFireTick = false;
    public static boolean doImmediateRespawn = false;
    public static boolean doMobSpawning = false;
    public static boolean fallDamage = false;
    public static boolean keepInventory = false;
    public static boolean mobGriefing = false;
    public static boolean naturalRegeneration = false;
    public static boolean showDeathMessages = false;
//spawnRadius

    public static void showWorldGamerulesUI() {
        MinecraftClient client = MinecraftClient.getInstance();
                try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
                    if (ctx == null) {
                        return;
                    }
                    boolean shouldSyncGamerules = false;

                    ImGui.begin("Gamerules", UIManager.showWorldGamerulesUI);
                    ImGui.textWrapped("View and set the world's gamerules.");

                    // Do Daylight Cycle Gamerule
                    if (ImGui.checkbox("Do Daylight Cycle", doDaylightCycle)) {
                        doDaylightCycle = !doDaylightCycle;
                        client.player.sendMessage(Text.of("DoDaylightCycle Set To: " + doDaylightCycle), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not time passes.");

                    // Do Weather Cycle Gamerule
                    if (ImGui.checkbox("Do Weather Cycle", doWeatherCycle)) {
                        doWeatherCycle = !doWeatherCycle;
                        client.player.sendMessage(Text.of("DoWeatherCycle Set To: " + doWeatherCycle), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not weather cycles naturally.");

                    // Send Command Feedback Gamerule
                    if (ImGui.checkbox("Send Command Feedback", sendCommandFeedback)) {
                        sendCommandFeedback = !sendCommandFeedback;
                        client.player.sendMessage(Text.of("SendCommandFeedback Set To: " + sendCommandFeedback), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not the result of commands is printed in chat.");

                    // Command Block Output Gamerule
                    if (ImGui.checkbox("Send Command Block Output", commandBlockOutput)) {
                        commandBlockOutput = !commandBlockOutput;
                        client.player.sendMessage(Text.of("CommandBlockOutput Set To: " + commandBlockOutput), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not command blocks print their output to operators.");

                    // Do Fire Tick Gamerule
                    if (ImGui.checkbox("Do Fire Tick", doFireTick)) {
                        doFireTick = !doFireTick;
                        client.player.sendMessage(Text.of("DoFireTick Set To: " + doFireTick), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or note fire spreads or withers over time.");

                    // Do Immediate Respawn Gamerule
                    if (ImGui.checkbox("Do Immediate Respawn", doImmediateRespawn)) {
                        doImmediateRespawn = !doImmediateRespawn;
                        client.player.sendMessage(Text.of("Do Immediate Respawn Set To: " + doImmediateRespawn), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not players respawn instantly instead of going to the death screen.");

                    // Do Mob Spawning Gamerule
                    if (ImGui.checkbox("Do Mob Spawning", doMobSpawning)) {
                        doMobSpawning = !doMobSpawning;
                        client.player.sendMessage(Text.of("Do Mob Spawning Set To: " + doMobSpawning), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not hostile mobs spawn in the world.");

                    // Fall Damage Gamerule
                    if (ImGui.checkbox("Enable Fall Damage", fallDamage)) {
                        fallDamage = !fallDamage;
                        client.player.sendMessage(Text.of("Enable Fall Damage Set To: " + fallDamage), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not fall damage is enabled.");

                    // Keep Inventory Gamerule
                    if (ImGui.checkbox("Enable Keep Inventory", keepInventory)) {
                        keepInventory = !keepInventory;
                        client.player.sendMessage(Text.of("Enable Keep Inventory Set To: " + keepInventory), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not players keeping their inventory on death is enabled.");

                    // Mob Griefing Gamerule
                    if (ImGui.checkbox("Enable Mob Griefing", mobGriefing)) {
                        mobGriefing = !mobGriefing;
                        client.player.sendMessage(Text.of("Enable Mob Griefing Set To: " + mobGriefing), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not hostile mobs can destroy blocks.");

                    // Natural Regeneration Gamerule
                    if (ImGui.checkbox("Enable Natural Regeneration", naturalRegeneration)) {
                        naturalRegeneration = !naturalRegeneration;
                        client.player.sendMessage(Text.of("Enable Natural Regeneration Set To: " + naturalRegeneration), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not players can passively regenerate health.");

                    // Show Death Messages Gamerule
                    if (ImGui.checkbox("Show Death Messages", showDeathMessages)) {
                        showDeathMessages = !showDeathMessages;
                        client.player.sendMessage(Text.of("Show Death Messages Set To: " + showDeathMessages), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not the messages showing how a player died are shown in chat.");

                    ImGui.end();
                    if (shouldSyncGamerules) {
                        ClientPlayNetworking.send(new SyncGamerulesDataPayload(
                                doDaylightCycle,
                                doWeatherCycle,
                                sendCommandFeedback,
                                commandBlockOutput,
                                doFireTick,
                                doImmediateRespawn,
                                doMobSpawning,
                                fallDamage,
                                keepInventory,
                                mobGriefing,
                                naturalRegeneration,
                                showDeathMessages
                        ));
                    }
        }
    }
}