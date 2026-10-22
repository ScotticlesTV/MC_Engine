/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's PointLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.PointLightData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.UUID;

public class MCEPointLightData extends PointLightData {
    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;

    // ---------- Packet Encoding & Decoding ----------

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, MCEPointLightData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (MCEPointLightData value, RegistryByteBuf buf) -> {
                buf.writeString(value.name);
                buf.writeUuid(value.uuid);
                buf.writeFloat(value.brightness);
                buf.writeFloat(value.color.red());
                buf.writeFloat(value.color.green());
                buf.writeFloat(value.color.blue());
                buf.writeDouble(value.position.x);
                buf.writeDouble(value.position.y);
                buf.writeDouble(value.position.z);
                buf.writeFloat(value.radius);
                buf.writeBoolean(value.occlusionEnabled);
                buf.writeFloat(value.inscattering);
            },
            // Packet Decoder
            (RegistryByteBuf buf) -> {
                String name = buf.readString();
                UUID uuid = buf.readUuid();
                float brightness = buf.readFloat();
                float r = buf.readFloat();
                float g = buf.readFloat();
                float b = buf.readFloat();
                double posX = buf.readDouble();
                double posY = buf.readDouble();
                double posZ = buf.readDouble();
                float radius = buf.readFloat();
                boolean occlusionEnabled = buf.readBoolean();
                float inscattering = buf.readFloat();

                return new MCEPointLightData(name, uuid, brightness, r, g, b, posX, posY, posZ, radius, occlusionEnabled, inscattering);
            }
    );

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE point Light Data object with all parameters specified.
     * @param name the name of the MCE Point Light Data.
     * @param uuid the unique ID of the MCE Point Light Data.
     * @param brightness the brightness of the MCE Point Light Data.
     * @param r the red-value of the MCE Spot Light Data's color property.
     * @param g the green-value of the MCE Spot Light Data's color property.
     * @param b the blue-value of the MCE Spot Light Data's color property.
     * @param posX the MCE Spot Light Data's position's x-value.
     * @param posY the MCE Spot Light Data's position's y-value.
     * @param posZ the MCE Spot Light Data's position's z-value.
     * @param radius the radius the MCe Spot Light Data's light should emit to.
     * @param occlusionEnabled whether or not occlusion (shadows) is enabled on the MCE Point Light Data.
     * @param inScattering the intensity of the MCE Point Light Data's in-scattering.
     */
    public MCEPointLightData(String name, UUID uuid, float brightness, float r, float g, float b, double posX, double posY, double posZ, float radius, boolean occlusionEnabled, float inScattering) {
        this.setName(name);
        this.setUuid(uuid);
        this.setBrightness(brightness);
        this.setColor(r, g, b);
        this.setPosition(posX, posY, posZ);
        this.setRadius(radius);
        this.setOcclusionEnabled(occlusionEnabled);
        this.setInscatteringStrength(inScattering);
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the name parameter of the MCE Point Light Data object.
     * @return the object's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the name parameter of the MCE Point Light Data object.
     * @param name the name to set the object's name parameter to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the UUID parameter of the MCE Point Light Data object.
     * @return the object's UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the UUID parameter of the MCE Point Light Data object.
     * @param uuid the uuid to set the object's uuid parameter to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
