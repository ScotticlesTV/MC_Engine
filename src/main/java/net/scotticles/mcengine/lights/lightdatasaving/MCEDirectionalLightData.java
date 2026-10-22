/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's DirectionalLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.DirectionalLightData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.UUID;

public class MCEDirectionalLightData extends DirectionalLightData{

    // ---------- Packet Encoding & Decoding ----------

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, MCEDirectionalLightData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (MCEDirectionalLightData value, RegistryByteBuf buf) -> {
                buf.writeString(value.name);
                buf.writeUuid(value.uuid);
                buf.writeFloat(value.brightness);
                buf.writeFloat(value.color.red());
                buf.writeFloat(value.color.green());
                buf.writeFloat(value.color.blue());
                buf.writeFloat(value.direction.x());
                buf.writeFloat(value.direction.y);
                buf.writeFloat(value.direction.z());
                buf.writeBoolean(value.occlusionEnabled);
            },
            // Packet Decoder
            (RegistryByteBuf buf) -> {
                String name = buf.readString();
                UUID uuid = buf.readUuid();
                float brightness = buf.readFloat();
                float r = buf.readFloat();
                float g = buf.readFloat();
                float b = buf.readFloat();
                float directionX = buf.readFloat();
                float directionY = buf.readFloat();
                float directionZ = buf.readFloat();
                boolean occlusionEnabled = buf.readBoolean();

                return new MCEDirectionalLightData(name, uuid, brightness, r, g, b, directionX, directionY, directionZ, occlusionEnabled);
            }
    );

    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE Directional Light Data object with all parameters specified.
     * @param name the name of the MCE Directional Light Data.
     * @param uuid the unique ID of the MCE Directional Light Data.
     * @param brightness the brightness of the MCE Directional Light Data.
     * @param r the red-value of the MCE Directional Light Data's color property.
     * @param g the green-value of the MCE Directional Light Data's color property.
     * @param b the blue-value of the MCE Directional Light Data's color property.
     * @param directionX the MCE Directional Light Data's x direction value.
     * @param directionY the MCE Directional Light Data's y direction value.
     * @param directionZ the MCE Directional Light Data's z direction value.
     * @param occlusionEnabled whether or not occlusion (shadows) is enabled on the MCE Directional Light Data.
     */
    public MCEDirectionalLightData(String name, UUID uuid, float brightness, float r, float g, float b, float directionX, float directionY, float directionZ, boolean occlusionEnabled) {
        this.setName(name);
        this.setUuid(uuid);
        this.setBrightness(brightness);
        this.setColor(r, g, b);
        this.setDirection(directionX, directionY, directionZ);
        this.setOcclusionEnabled(occlusionEnabled);
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the name parameter of the MCE Directional Light Data object.
     * @return the object's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the name parameter of the MCE Directional Light Data object.
     * @param name the name to set the object's name parameter to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the UUID parameter of the MCE Directional Light Data object.
     * @return the object's UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the UUID parameter of the MCE Directional Light Data object.
     * @param uuid the uuid to set the object's uuid parameter to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
