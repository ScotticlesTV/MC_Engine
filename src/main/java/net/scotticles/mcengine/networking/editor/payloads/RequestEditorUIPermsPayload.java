package net.scotticles.mcengine.networking.editor.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestEditorUIPermsPayload() implements CustomPayload {
    public static final CustomPayload.Id<RequestEditorUIPermsPayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "request_editor_ui_perms_payload"));

    public static final PacketCodec<RegistryByteBuf, RequestEditorUIPermsPayload> CODEC =
            PacketCodec.unit(new RequestEditorUIPermsPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
