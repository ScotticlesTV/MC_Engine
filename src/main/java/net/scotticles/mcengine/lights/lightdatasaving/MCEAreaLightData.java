/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's AreaLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.AreaLightData;

import java.util.UUID;

public class MCEAreaLightData extends AreaLightData {
    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;
    private AreaLightData areaLightData;

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE Area Light Data object with all parameters specified.
     * @param name, the name of the MCE Area Light Data object.
     * @param uuid, the unique id of the MCE Area Light Data object.
     * @param areaLightData, the veil light of the MCE Area Light Data.
     */
    public MCEAreaLightData(String name, UUID uuid, AreaLightData areaLightData) {
        this.setName(name);
        this.setUuid(uuid);
        this.setAreaLightData(areaLightData);
    }

    // ---------- Getters & Setters ----------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public AreaLightData getAreaLightData() {
        return areaLightData;
    }

    private void setAreaLightData(AreaLightData areaLightData) {
        this.areaLightData = areaLightData;
    }
}
