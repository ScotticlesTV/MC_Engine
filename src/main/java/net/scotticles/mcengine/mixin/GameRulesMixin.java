package net.scotticles.mcengine.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.scotticles.mcengine.networking.gamerules.SyncGamerulesEditorPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRules.class)
public class GameRulesMixin {
    @Inject(method = "set", at = @At("HEAD"))
    private <T extends GameRules.Rule<T>> void onGameRuleChange(
            GameRules.Key<T> key,
            T value,
            MinecraftServer server,
            CallbackInfo ci
    ) {
        if (key.equals(GameRules.DO_DAYLIGHT_CYCLE) || key.equals(GameRules.DO_WEATHER_CYCLE) || key.equals(GameRules.SEND_COMMAND_FEEDBACK)) {
            //Sync Gamerules To All Clients
            GameRules gameRules = server.getGameRules();

            // Sync GameRules To All Client Editors
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, new SyncGamerulesEditorPayload(gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).get(), gameRules.get(GameRules.DO_WEATHER_CYCLE).get(), gameRules.get(GameRules.SEND_COMMAND_FEEDBACK).get()));
            }
        }
    }
}
