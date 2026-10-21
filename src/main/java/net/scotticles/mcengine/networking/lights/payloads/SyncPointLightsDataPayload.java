package net.scotticles.mcengine.networking.lights.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEPointLightData;

import java.util.ArrayList;
import java.util.List;

public record SyncPointLightsDataPayload(List<MCEPointLightData> pointLights) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final Id<SyncPointLightsDataPayload> ID =
            new Id<>(Identifier.of("mc_engine", "sync_point_lights_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, SyncPointLightsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, MCEPointLightData.PACKET_CODEC), SyncPointLightsDataPayload::pointLights,
            SyncPointLightsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
