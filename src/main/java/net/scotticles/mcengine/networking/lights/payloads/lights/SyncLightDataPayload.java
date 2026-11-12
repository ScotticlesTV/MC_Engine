package net.scotticles.mcengine.networking.lights.payloads.lights;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.lights.lightdata.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdata.MCEDirectionalLightData;
import net.scotticles.mcengine.lights.lightdata.MCEPointLightData;
import net.scotticles.mcengine.lights.lightdata.MCESpotLightData;

import java.util.ArrayList;
import java.util.List;

public record SyncLightDataPayload(List<MCEAreaLightData> areaLights, List<MCEPointLightData> pointLights, List<MCEDirectionalLightData> directionalLights, List<MCESpotLightData> spotLights) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final Id<SyncLightDataPayload> ID =
            new Id<>(Identifier.of("mc_engine", "sync_light_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, SyncLightDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, MCEAreaLightData.PACKET_CODEC), SyncLightDataPayload::areaLights,
            PacketCodecs.collection(ArrayList::new, MCEPointLightData.PACKET_CODEC), SyncLightDataPayload::pointLights,
            PacketCodecs.collection(ArrayList::new, MCEDirectionalLightData.PACKET_CODEC), SyncLightDataPayload::directionalLights,
            PacketCodecs.collection(ArrayList::new, MCESpotLightData.PACKET_CODEC), SyncLightDataPayload::spotLights,
            SyncLightDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
