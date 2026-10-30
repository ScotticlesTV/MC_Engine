package net.scotticles.mcengine.networking.lights.payloads.lightprofiles;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public record SyncLightProfileEditorPayload(ArrayList<String> lpNames, String newLPName, String message) implements CustomPayload {
    public static final Id<SyncLightProfileEditorPayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "sync_light_profile_editor_packet"));

    public static final PacketCodec<RegistryByteBuf, SyncLightProfileEditorPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING.collect(PacketCodecs.toCollection(ArrayList::new)),
            SyncLightProfileEditorPayload::lpNames,
            PacketCodecs.STRING, SyncLightProfileEditorPayload::newLPName,
            PacketCodecs.STRING, SyncLightProfileEditorPayload::message,
            SyncLightProfileEditorPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}