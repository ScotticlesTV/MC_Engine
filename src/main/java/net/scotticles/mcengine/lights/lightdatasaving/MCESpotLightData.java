/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's SpotLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.SpotLightData;

import java.util.UUID;

public class MCESpotLightData {

    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;
    private SpotLightData spotLightData;

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE Spot Light Data object with all parameters specified.
     * @param name, the name of the MCE Spot Light Data object.
     * @param uuid, the unique id of the MCE Spot Light Data object.
     * @param spotLightData, the veil light of the MCE Spot Light Data.
     */
    public MCESpotLightData(String name, UUID uuid, SpotLightData spotLightData) {
        this.setName(name);
        this.setUuid(uuid);
        this.setSpotLightData(spotLightData);
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the name parameter of the MCE Spot Light Data object.
     * @return, the object's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the name parameter of the MCE Spot Light Data object.
     * @param name, the name to set the object's name parameter to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the UUID parameter of the MCE Spot Light Data object.
     * @return, the object's UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the UUID parameter of the MCE Spot Light Data object.
     * @param uuid, the uuid to set the object's uuid parameter to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Retrieve the spot light data object parameter of the MCE Spot Light Data object.
     * @return, the object's spot light data object.
     */
    public SpotLightData getSpotLightData() {
        return this.spotLightData;
    }

    /**
     * Assign the spot light data object property of the MCE Spot Light Data object.
     * @param spotLightData, the spot light data object to set the object's spot light data object parameter to.
     */
    private void setSpotLightData(SpotLightData spotLightData) {
        this.spotLightData = spotLightData;
    }
}
