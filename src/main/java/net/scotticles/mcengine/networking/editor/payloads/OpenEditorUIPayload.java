package net.scotticles.mcengine.networking.editor.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenEditorUIPayload(boolean allowed) implements CustomPayload {
    public static final CustomPayload.Id<OpenEditorUIPayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "open_editor_ui_packet"));

    public static final PacketCodec<RegistryByteBuf, OpenEditorUIPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, OpenEditorUIPayload::allowed,
            OpenEditorUIPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}