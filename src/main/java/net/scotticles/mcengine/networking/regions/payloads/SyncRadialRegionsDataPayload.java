package net.scotticles.mcengine.networking.regions.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;

import java.util.ArrayList;
import java.util.List;

public record SyncRadialRegionsDataPayload(List<RadialRegionData> radialRegions) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final CustomPayload.Id<SyncRadialRegionsDataPayload> ID =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "sync_radial_regions_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, SyncRadialRegionsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, RadialRegionData.PACKET_CODEC), SyncRadialRegionsDataPayload::radialRegions,
            SyncRadialRegionsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
