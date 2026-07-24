package net.scotticles.mcengine.networking.gamerules.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncGamerulesDataPayload(boolean doDaylightCycle, boolean doWeatherCycle, boolean sendCommandFeedback) implements CustomPayload {
    public static final CustomPayload.Id<SyncGamerulesDataPayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "sync_gamerules_data_packet"));

    public static final PacketCodec<RegistryByteBuf, SyncGamerulesDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, SyncGamerulesDataPayload::doDaylightCycle,
            PacketCodecs.BOOL, SyncGamerulesDataPayload::doWeatherCycle,
            PacketCodecs.BOOL, SyncGamerulesDataPayload::sendCommandFeedback,
            SyncGamerulesDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}