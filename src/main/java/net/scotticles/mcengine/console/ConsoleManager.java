package net.scotticles.mcengine.console;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scotticles.mcengine.networking.common.SendMessagePayload;

import java.util.ArrayList;
import java.util.List;

public class ConsoleManager {

    public static List<String> consoleMessages = new ArrayList<>();
    private static final int maxConsoleLength = 100;

    /**
     * Client side method to make sure the console doesn't exceed its max length.
     * @param newMessage the message to add to the console.
     */
    public static void handleConsoleMessages(String newMessage) {
        if (consoleMessages.size() > maxConsoleLength) {
            consoleMessages.removeFirst();
            consoleMessages.add(newMessage);
        } else {
            consoleMessages.add(newMessage);
        }
    }

    //TODO: Would it be possible to add player tags to each message? So the name of player that made the change is broadcasted as well.
    /**
     * Server-Side method to broadcast a message to all players on the server's MC Engine console.
     * @param message the message to send as a String.
     * @param server the server to get the player list from.
     */
    public static void broadcastMessage(String message, MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, new SendMessagePayload(message));
        }
    }
}