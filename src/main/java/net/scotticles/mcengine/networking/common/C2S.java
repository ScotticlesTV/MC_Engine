package net.scotticles.mcengine.networking.common;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.scotticles.mcengine.networking.editor.payloads.CheckEditorUIPermsPayload;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.*;
import net.scotticles.mcengine.networking.lights.payloads.lightprofiles.DeleteLightProfilePayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncLightDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRadialRegionsDataPayload;
import net.scotticles.mcengine.networking.sound.SyncSoundSettingsPayload;
import net.scotticles.mcengine.networking.time.payloads.SetTimePayload;
import net.scotticles.mcengine.networking.weather.payloads.ChangeWeatherPayload;

public class C2S {
    // Register Channels For Packets To Travel On From Client To Server
    public static void registerPacketChannels() {
        PayloadTypeRegistry.playC2S().register(SyncRadialRegionsDataPayload.ID, SyncRadialRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncLinearRegionsDataPayload.ID, SyncLinearRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(CheckEditorUIPermsPayload.TYPE, CheckEditorUIPermsPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncGamerulesDataPayload.TYPE, SyncGamerulesDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ChangeWeatherPayload.TYPE, ChangeWeatherPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SetTimePayload.TYPE, SetTimePayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncLightDataPayload.ID, SyncLightDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SaveLightsDataPayload.TYPE, SaveLightsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(LoadLightsDataPayload.TYPE, LoadLightsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(DeleteLightProfilePayload.TYPE, DeleteLightProfilePayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncSoundSettingsPayload.TYPE, SyncSoundSettingsPayload.CODEC);
    }
}