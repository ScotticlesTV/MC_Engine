package net.scotticles.mcengine.networking.lights.payloads.lights;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.lights.lightdatasaving.MCEDirectionalLightData;

import java.util.ArrayList;
import java.util.List;

public record SyncDirectionalLightsDataPayload(List<MCEDirectionalLightData> directionalLights) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final Id<SyncDirectionalLightsDataPayload> ID =
            new Id<>(Identifier.of("mc_engine", "sync_directional_lights_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, SyncDirectionalLightsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, MCEDirectionalLightData.PACKET_CODEC), SyncDirectionalLightsDataPayload::directionalLights,
            SyncDirectionalLightsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
