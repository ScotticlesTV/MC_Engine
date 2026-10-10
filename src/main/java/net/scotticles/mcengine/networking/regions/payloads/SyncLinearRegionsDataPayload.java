package net.scotticles.mcengine.networking.regions.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;

import java.util.HashSet;
import java.util.Set;

public record SyncLinearRegionsDataPayload(Set<LinearRegionData> linearRegionData) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final Id<SyncLinearRegionsDataPayload> ID =
            new Id<>(Identifier.of("mc_engine", "sync_linear_regions_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, SyncLinearRegionsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(HashSet::new, LinearRegionData.PACKET_CODEC), SyncLinearRegionsDataPayload::linearRegionData,
            SyncLinearRegionsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}