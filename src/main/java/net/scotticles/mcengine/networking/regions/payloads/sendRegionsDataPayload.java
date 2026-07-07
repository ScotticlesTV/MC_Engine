package net.scotticles.mcengine.networking.regions.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;

import java.util.HashSet;
import java.util.Set;

public record sendRegionsDataPayload(Set<RegionData> regions) implements CustomPayload {

    // Define a unique ID for your packet channel
    public static final CustomPayload.Id<sendRegionsDataPayload> ID =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "send_regions_data_packet"));

    // Define a codex to serialize and deserialize the data
    public static final PacketCodec<RegistryByteBuf, sendRegionsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.collection(HashSet::new, RegionData.PACKET_CODEC), sendRegionsDataPayload::regions,
            sendRegionsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
