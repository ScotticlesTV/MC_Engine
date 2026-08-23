package net.scotticles.mcengine.interactions;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.scotticles.mcengine.interactions.interactiondatasaving.InteractionData;

import java.util.Set;
import java.util.UUID;

public class InteractionEffectsManager {

    //Initiate Interactions Functionality
    public static void interactionEffectsInit(Set<InteractionData> interactions) {
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                // Get The Player's UUID To More Easily Track Them
                UUID playerUuid = player.getUuid();

                // Loop Through All Regions To Enable Effects For All Of Them
                for  (InteractionData interaction : interactions) {

                    // Confirm That The Player Is In The Overworld
                    // (Can Be Changed Later To Have Interactions In Multiple Dimensions)
                    // (Just Need To Add A "Select Dimension" Checkbox To Each Interaction)
                    if (player.getWorld().getRegistryKey() == World.OVERWORLD) {

                        // Check If The Player Is Looking At The Region (Within The Allowed Range Of Offset)

                        // Check If The Interaction Key Has Been Pressed

                        // If Yes To Both, Run The Interaction Commands

                        // Apply A Potential Cooldown
                }
            }
        }
    }
    );
    }
}