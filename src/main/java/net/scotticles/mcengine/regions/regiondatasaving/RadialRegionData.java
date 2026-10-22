package net.scotticles.mcengine.regions.regiondatasaving;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;

import java.util.*;

public class RadialRegionData {

    // ---------- Instance Properties ----------

    private UUID regionUuid;
    private String regionName;
    private int regionX;
    private int regionY;
    private int regionZ;
    private int regionRadius;
    private Set<UUID> playersInside;
    private boolean regionEnabled;
    private List<String> regionEnterCommands;
    private List<String> regionExitCommands;
    private String regionSound;
    private float regionSoundVolume;
    private float regionSoundFadeDuration;

    // ---------- Packet Encoding & Decoding ----------

    // Codecs For Region Enter, Exit, And UUID Sets
    private static final PacketCodec<RegistryByteBuf, List<String>> STRING_LIST_CODEC =
            PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING);

    private static final PacketCodec<RegistryByteBuf, Set<UUID>> UUID_SET_CODEC =
            PacketCodecs.collection(HashSet::new, Uuids.PACKET_CODEC);

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, RadialRegionData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (RadialRegionData value, RegistryByteBuf buf) -> {
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

                return new RadialRegionData(uuid, name, x, y, z, radius, enterCommands, exitCommands, players, enabled, sound, soundVolume, soundFadeDuration);
            }
    );

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new Radial Region Data object with all parameters specified.
     * @param regionUuid
     * @param regionName
     * @param regionX
     * @param regionY
     * @param regionZ
     * @param regionRadius
     * @param regionEnterCommands
     * @param regionExitCommands
     * @param playersInside
     * @param regionEnabled
     * @param regionSound
     * @param regionSoundVolume
     * @param regionSoundFadeDuration
     */
    public RadialRegionData(UUID regionUuid, String regionName, int regionX, int regionY, int regionZ, int regionRadius, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        this.setRegionUuid(regionUuid);
        this.setRegionName(regionName);
        this.setRegionPos(regionX, regionY, regionZ);
        this.setRegionRadius(regionRadius);
        this.setRegionEnterCommands(regionEnterCommands);
        this.setRegionExitCommands(regionExitCommands);
        this.setPlayersInside(playersInside);
        this.setRegionEnabled(regionEnabled);
        this.setRegionSound(regionSound);
        this.setRegionSoundVolume(regionSoundVolume);
        this.setRegionSoundFadeDuration(regionSoundFadeDuration);
    }

    // ---------- Getters & Setters ----------

    public UUID getRegionUuid() {
        return this.regionUuid;
    }

    private void setRegionUuid(UUID regionUuid) {
        this.regionUuid = regionUuid;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getRegionX() {
        return this.regionX;
    }

    public void setRegionX(int regionX) {
        this.regionX = regionX;
    }

    public int getRegionY() {
        return this.regionY;
    }

    public void setRegionY(int regionY) {
        this.regionY = regionY;
    }

    public int getRegionZ() {
        return this.regionZ;
    }

    public void setRegionZ(int regionZ) {
        this.regionZ = regionZ;
    }

    public void setRegionPos(int regionX, int regionY, int regionZ) {
        this.regionX = regionX;
        this.regionY = regionY;
        this.regionZ = regionZ;
    }

    public int getRegionRadius() {
        return this.regionRadius;
    }

    public void setRegionRadius(int regionRadius) {
        this.regionRadius = regionRadius;
    }

    public Set<UUID> getPlayersInside() {
        return this.playersInside;
    }

    public void setPlayersInside(Set<UUID> playersInside) {
        this.playersInside = playersInside;
    }

    public boolean isRegionEnabled() {
        return this.regionEnabled;
    }

    public void setRegionEnabled(boolean regionEnabled) {
        this.regionEnabled = regionEnabled;
    }

    public List<String> getRegionEnterCommands() {
        return this.regionEnterCommands;
    }

    public void setRegionEnterCommands(List<String> regionEnterCommands) {
        this.regionEnterCommands = regionEnterCommands;
    }

    public List<String> getRegionExitCommands() {
        return this.regionExitCommands;
    }

    public void setRegionExitCommands(List<String> regionExitCommands) {
        this.regionExitCommands = regionExitCommands;
    }

    public String getRegionSound() {
        return this.regionSound;
    }

    public void setRegionSound(String regionSound) {
        this.regionSound = regionSound;
    }

    public float getRegionSoundVolume() {
        return this.regionSoundVolume;
    }

    public void setRegionSoundVolume(float regionSoundVolume) {
        this.regionSoundVolume = regionSoundVolume;
    }

    public float getRegionSoundFadeDuration() {
        return this.regionSoundFadeDuration;
    }

    public void setRegionSoundFadeDuration(float regionSoundFadeDuration) {
        this.regionSoundFadeDuration = regionSoundFadeDuration;
    }
}
