package net.scotticles.mcengine.networking.lights.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCESpotLightData;

import java.util.ArrayList;
import java.util.List;

public record SyncSpotLightsDataPayload(List<MCESpotLightData> spotLights) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final Id<SyncSpotLightsDataPayload> ID =
            new Id<>(Identifier.of("mc_engine", "sync_spot_lights_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, SyncSpotLightsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, MCESpotLightData.PACKET_CODEC), SyncSpotLightsDataPayload::spotLights,
            SyncSpotLightsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
