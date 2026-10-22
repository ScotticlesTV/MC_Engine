/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's SpotLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.SpotLightData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import java.util.UUID;

public class MCESpotLightData extends SpotLightData {

    // ---------- Packet Encoding & Decoding ----------

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, MCESpotLightData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (MCESpotLightData value, RegistryByteBuf buf) -> {
                buf.writeString(value.name);
                buf.writeUuid(value.uuid);
                buf.writeFloat(value.brightness);
                buf.writeFloat(value.color.red());
                buf.writeFloat(value.color.green());
                buf.writeFloat(value.color.blue());
                buf.writeFloat(value.size);
                buf.writeDouble(value.position.x);
                buf.writeDouble(value.position.y);
                buf.writeDouble(value.position.z);
                buf.writeQuaternionf(value.orientation);
                buf.writeFloat(value.angle);
                buf.writeFloat(value.distance);
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
                float size = buf.readFloat();
                double posX = buf.readDouble();
                double posY = buf.readDouble();
                double posZ = buf.readDouble();
                Quaternionf orientation = buf.readQuaternionf();
                float angle = buf.readFloat();
                float distance = buf.readFloat();
                boolean occlusionEnabled = buf.readBoolean();
                float inscattering = buf.readFloat();

                return new MCESpotLightData(name, uuid, brightness, r, g, b, size, posX, posY, posZ, orientation, angle, distance, occlusionEnabled, inscattering);
            }
    );

    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE Spot Light Data with all parameters specified.
     * @param name the name of the MCE Spot Light Data.
     * @param brightness the brightness of the MCE Spot Light Data.
     * @param r the red-value of the MCE Spot Light Data's color property.
     * @param g the green-value of the MCE Spot Light Data's color property.
     * @param b the blue-value of the MCE Spot Light Data's color property.
     * @param size the size of the MCE Spot Light Data.
     * @param posX the MCE Spot Light Data's position's x-value.
     * @param posY the MCE Spot Light Data's position's y-value.
     * @param posZ the MCE Spot Light Data's position's z-value.
     * @param orientation the orientation/emission direction of the MCE Spot Light Data.
     * @param angle the angle the MCE Spot Light Data's light should emit from its origin point.
     * @param distance the distance the MCE Spot Light Data should travel.
     * @param occlusionEnabled whether or not occlusion (shadows) is enabled on the MCE Spot Light Data.
     * @param inScattering the intensity of the MCE Spot Light Data's in-scattering.
     */
    public MCESpotLightData(String name, UUID uuid, float brightness, float r, float g, float b, float size, double posX, double posY, double posZ, Quaternionfc orientation, float angle, float distance, boolean occlusionEnabled, float inScattering) {
        this.setName(name);
        this.setUuid(uuid);
        this.setBrightness(brightness);
        this.setColor(r, g, b);
        this.setSize(size);
        this.setPosition(posX, posY, posZ);
        this.setOrientation(orientation.x(), orientation.y(), orientation.z(), orientation.w());
        this.setAngle(angle);
        this.setDistance(distance);
        this.setOcclusionEnabled(occlusionEnabled);
        this.setInscatteringStrength(inScattering);
    }

    /**
     * Copy Constructor: Construct a new MCE Spot Light Data with values taken from an already existing MCE Spot Light Data.
     * @param mceSpotLightData the MCE Spot Light Data to copy values from.
     */
    public MCESpotLightData(MCESpotLightData mceSpotLightData) {
        this(
                mceSpotLightData.getName(),
                UUID.randomUUID(),
                mceSpotLightData.getBrightness(),
                mceSpotLightData.getColor().red(),
                mceSpotLightData.getColor().green(),
                mceSpotLightData.getColor().blue(),
                mceSpotLightData.getSize(),
                mceSpotLightData.getPosition().x(),
                mceSpotLightData.getPosition().y(),
                mceSpotLightData.getPosition().z(),
                mceSpotLightData.getOrientation(),
                mceSpotLightData.getAngle(),
                mceSpotLightData.getDistance(),
                mceSpotLightData.isOcclusionEnabled(),
                mceSpotLightData.getInscatteringStrength()
        );
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the name parameter of the MCE Spot Light Data object.
     * @return the object's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the name parameter of the MCE Spot Light Data object.
     * @param name the name to set the object's name parameter to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the UUID parameter of the MCE Spot Light Data object.
     * @return the object's UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the UUID parameter of the MCE Spot Light Data object.
     * @param uuid the uuid to set the object's uuid parameter to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Assign the position of the MCE Spot Light Data object.
     * @param x the x-value of the new position.
     * @param y the y-value of the new position.
     * @param z the z-value of the new position.
     */
    public void setPosition(double x, double y, double z) {
        this.position.set(x, y, z);
    }

    /**
     * Assign the orientation of the MCE Spot Light Data object.
     * @param x the x-value of the new orientation.
     * @param y the y-value of the new orientation.
     * @param z the z-value of the new orientation.
     * @param w the w-value of the new orientation.
     */
    public void setOrientation(float x, float y, float z, float w) {
        this.orientation.set(x, y, z, w);
    }
}
