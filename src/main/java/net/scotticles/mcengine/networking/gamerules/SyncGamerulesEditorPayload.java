package net.scotticles.mcengine.networking.gamerules;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncGamerulesEditorPayload(boolean doDaylightCycle, boolean doWeatherCycle, boolean sendCommandFeedback) implements CustomPayload {
    public static final CustomPayload.Id<SyncGamerulesEditorPayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "sync_gamerules_editor_packet"));

    public static final PacketCodec<RegistryByteBuf, SyncGamerulesEditorPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, SyncGamerulesEditorPayload::doDaylightCycle,
            PacketCodecs.BOOL, SyncGamerulesEditorPayload::doWeatherCycle,
            PacketCodecs.BOOL, SyncGamerulesEditorPayload::sendCommandFeedback,
            SyncGamerulesEditorPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}