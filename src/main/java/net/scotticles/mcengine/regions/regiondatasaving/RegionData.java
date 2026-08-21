package net.scotticles.mcengine.regions.regiondatasaving;

import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;

import java.util.*;

public class RegionData {

    // Region Data Variables
    public UUID regionUuid;
    public String regionName;
    public int regionX;
    public int regionY;
    public int regionZ;
    public int regionRadius;
    public Set<UUID> playersInside;
    public boolean regionEnabled;
    public List<String> regionEnterCommands;
    public List<String> regionExitCommands;
    public String regionSound;
    public float regionSoundVolume;
    public float regionSoundFadeDuration;




    // Codecs For Region Enter, Exit, And UUID Sets
    private static final PacketCodec<RegistryByteBuf, List<String>> STRING_LIST_CODEC =
            PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING);

    private static final PacketCodec<RegistryByteBuf, Set<UUID>> UUID_SET_CODEC =
            PacketCodecs.collection(HashSet::new, Uuids.PACKET_CODEC);

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, RegionData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (RegionData value, RegistryByteBuf buf) -> {
                buf.writeUuid(value.regionUuid);
                buf.writeString(value.regionName);
                buf.writeInt(value.regionX);
                buf.writeInt(value.regionY);
                buf.writeInt(value.regionZ);
                buf.writeInt(value.regionRadius);
                STRING_LIST_CODEC.encode(buf, value.regionEnterCommands);
                STRING_LIST_CODEC.encode(buf, value.regionExitCommands);
                UUID_SET_CODEC.encode(buf, value.playersInside);
                buf.writeBoolean(value.regionEnabled);
                buf.writeString(value.regionSound);
                buf.writeFloat(value.regionSoundVolume);
                buf.writeFloat(value.regionSoundFadeDuration);
            },
            // Packet Decoder
            (RegistryByteBuf buf) -> {
                UUID uuid = buf.readUuid();
                String name = buf.readString();
                int x = buf.readInt();
                int y = buf.readInt();
                int z = buf.readInt();
                int radius = buf.readInt();
                List<String> enterCommands = STRING_LIST_CODEC.decode(buf);
                List<String> exitCommands = STRING_LIST_CODEC.decode(buf);
                Set<UUID> players = UUID_SET_CODEC.decode(buf);
                boolean enabled = buf.readBoolean();
                String sound = buf.readString();
                float soundVolume = buf.readFloat();
                float soundFadeDuration = buf.readFloat();

                return new RegionData(uuid, name, x, y, z, radius, enterCommands, exitCommands, players, enabled, sound, soundVolume, soundFadeDuration);
            }
    );

    public RegionData(UUID regionUuid, String regionName, int regionX, int regionY, int regionZ, int regionRadius, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        this.regionUuid = regionUuid;
        this.regionName = regionName;
        this.regionX = regionX;
        this.regionY = regionY;
        this.regionZ = regionZ;
        this.regionRadius = regionRadius;
        this.regionEnterCommands = new ArrayList<>(regionEnterCommands);
        this.regionExitCommands = new ArrayList<>(regionExitCommands);
        this.playersInside = playersInside;
        this.regionEnabled = regionEnabled;
        this.regionSound = regionSound;
        this.regionSoundVolume = regionSoundVolume;
        this.regionSoundFadeDuration = regionSoundFadeDuration;
    }

    public RegionData toRegionsData() {
        return new RegionData(regionUuid, regionName, regionX, regionY, regionZ, regionRadius, new ArrayList<>(regionEnterCommands), new ArrayList<>(regionExitCommands), playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);
    }
}
