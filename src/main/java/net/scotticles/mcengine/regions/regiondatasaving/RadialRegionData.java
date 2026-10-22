/**
 * Author: Scotticles
 * Description: A class defining a spherical area that the player can interact with by entering and exiting.
 */
package net.scotticles.mcengine.regions.regiondatasaving;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;

import java.util.*;

public class RadialRegionData {

    // ---------- Instance Properties ----------

    private UUID uuid;
    private String name;
    private int x;
    private int y;
    private int z;
    private int radius;
    private Set<UUID> playersInside;
    private boolean enabled;
    private List<String> enterCommands;
    private List<String> exitCommands;
    private String sound;
    private float soundVolume;
    private float soundFadeDuration;

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
                buf.writeUuid(value.uuid);
                buf.writeString(value.name);
                buf.writeInt(value.x);
                buf.writeInt(value.y);
                buf.writeInt(value.z);
                buf.writeInt(value.radius);
                STRING_LIST_CODEC.encode(buf, value.enterCommands);
                STRING_LIST_CODEC.encode(buf, value.exitCommands);
                UUID_SET_CODEC.encode(buf, value.playersInside);
                buf.writeBoolean(value.enabled);
                buf.writeString(value.sound);
                buf.writeFloat(value.soundVolume);
                buf.writeFloat(value.soundFadeDuration);
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
     * @param uuid, the unique ID of the Radial Region.
     * @param name, the name of the Radial Region.
     * @param x, the x-value of the Radial Region's location.
     * @param y, the y-value of the Radial Region's location.
     * @param z, the z-value of the Radial Region's location.
     * @param radius, the radius of the Radial Region.
     * @param enterCommands, the Radial Region's enter commands.
     * @param exitCommands, the Radial Region's exit commands.
     * @param playersInside, the player's currently inside the Radial Region's bounds.
     * @param enabled, whether or not the Radial Region is currently enabled.
     * @param sound, the sound to play when a player enters the Radial Region.
     * @param soundVolume, the volume to play the Radial Region's sound at.
     * @param soundFadeDuration, the time it should take for the Radial Region's sound to fade when a player exits the region.
     */
    public RadialRegionData(UUID uuid, String name, int x, int y, int z, int radius, List<String> enterCommands, List<String> exitCommands, Set<UUID> playersInside, boolean enabled, String sound, float soundVolume, float soundFadeDuration) {
        this.setUuid(uuid);
        this.setName(name);
        this.setPos(x, y, z);
        this.setRadius(radius);
        this.setEnterCommands(enterCommands);
        this.setExitCommands(exitCommands);
        this.setPlayersInside(playersInside);
        this.setEnabled(enabled);
        this.setSound(sound);
        this.setSoundVolume(soundVolume);
        this.setSoundFadeDuration(soundFadeDuration);
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the Radial Region Data's uuid property.
     * @return the Radial Region Data's uuid.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the Radial Region Data's uuid property.
     * @param uuid the uuid to set the Radial Region Data's uuid property to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Retrieve the Radial Region Data's name property.
     * @return the Radial Region Data's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the Radial Region Data's name property.
     * @param name the Radial Region Data's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the x-value of the Radial Region Data's current position.
     * @return the x-value of the Radial Region Data's current position.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Assign the x-value of the Radial Region Data's current position.
     * @param x the x-value to set the Radial Region Data's current position to.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Retrieve the y-value of the Radial Region Data's current position.
     * @return the y-value of the Radial Region Data's current position.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Assign the y-value of the Radial Region Data's current position.
     * @param y the y-value to set the Radial Region Data's current position to.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Retrieve the z-value of the Radial Region Data's current position.
     * @return the z-value of the Radial Region Data's current position.
     */
    public int getZ() {
        return this.z;
    }

    /**
     * Assign the z-value of the Radial Region Data's current position.
     * @param z the z-value to set the Radial Region Data's current position to.
     */
    public void setZ(int z) {
        this.z = z;
    }

    /**
     * Assign the entire position of the Radial Region Data.
     * @param x the x-value of the Radial Region Data's new position.
     * @param y the y-value of the Radial Region Data's new position.
     * @param z the z-value of the Radial Region Data's new position.
     */
    public void setPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Retrieve the radius property of the Radial Region Data (the distance the region extends in all directions from its position).
     * @return the Radial Region Data's radius property.
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Assign the radius property of the Radial Region Data (the distance the region extends in all directions from its position).
     * @param radius the radius to set the Radial Region Data's radius property to.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Retrieve the playersInside property of the Radial Region Data.
     * @return the set of players currently inside the Radial Region Data's bounds.
     */
    public Set<UUID> getPlayersInside() {
        return this.playersInside;
    }

    /**
     * Assign the playersInside property of the Radial Region Data.
     * @param playersInside, the set of players currently inside the Radial Region Data's bounds to set the playersInside property to.
     */
    public void setPlayersInside(Set<UUID> playersInside) {
        this.playersInside = playersInside;
    }

    /**
     * Retriever whether or not the Radial Region Data is currently enabled.
     * @return the Radial Region Data's enabled status.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Assign whether or not the Radial Region Data is currently enabled.
     * @param enabled, the enabled value to set the Radial Region Data's enabled property to.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Retrieve the Radial Region Data's enter commands.
     * @return the list of enter commands for the Radial Region Data.
     */
    public List<String> getEnterCommands() {
        return this.enterCommands;
    }

    /**
     * Assign the enter commands list property of the Radial Region Data.
     * @param enterCommands, the list of enter commands to set the Radial Region Data's enterCommands property to.
     */
    public void setEnterCommands(List<String> enterCommands) {
        this.enterCommands = enterCommands;
    }

    /**
     * Retrieve the Radial Region Data's enter commands.
     * @return the list of enter commands for the Radial Region Data.
     */
    public List<String> getExitCommands() {
        return this.exitCommands;
    }

    /**
     * Assign the exit commands list property of the Radial Region Data.
     * @param exitCommands, the list of exit commands to set the Radial Region Data's exitCommands property to.
     */
    public void setExitCommands(List<String> exitCommands) {
        this.exitCommands = exitCommands;
    }

    /**
     * Retrieve the sound property of the Radial Region Data.
     * @return the sound property of the Radial Region Data.
     */
    public String getSound() {
        return this.sound;
    }

    /**
     * Assign the sound property of the Radial Region Data.
     * @param sound, the sound to set the sound property of the Radial Region Data to.
     */
    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * Retrieve the sound volume property of the Radial Region Data.
     * @return the sound volume of the Radial Region Data.
     */
    public float getSoundVolume() {
        return this.soundVolume;
    }

    /**
     * Assign the sound volume property of the Radial Region Data.
     * @param soundVolume, the sound volume to set the Radial Region Data's sound property to.
     */
    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    /**
     * Get the sound fade duration property of the Radial Region Data.
     * @return the Radial Region Data's sound fade duration property.
     */
    public float getSoundFadeDuration() {
        return this.soundFadeDuration;
    }

    /**
     * Assign the sound fade duration property of the Radial Region Data.
     * @param soundFadeDuration, the sound fade duration value to set the Radial Region Data's sound fade duration property to.
     */
    public void setSoundFadeDuration(float soundFadeDuration) {
        this.soundFadeDuration = soundFadeDuration;
    }
}
