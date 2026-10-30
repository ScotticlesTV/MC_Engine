package net.scotticles.mcengine.networking.lights.payloads.lights;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;

import java.util.ArrayList;
import java.util.List;

public record SyncAreaLightsDataPayload(List<MCEAreaLightData> areaLights) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final Id<SyncAreaLightsDataPayload> ID =
            new Id<>(Identifier.of("mc_engine", "sync_area_lights_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, SyncAreaLightsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, MCEAreaLightData.PACKET_CODEC), SyncAreaLightsDataPayload::areaLights,
            SyncAreaLightsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
