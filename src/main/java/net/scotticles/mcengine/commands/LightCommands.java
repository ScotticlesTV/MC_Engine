package net.scotticles.mcengine.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.scotticles.mcengine.networking.lights.payloads.LoadLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.SaveLightsDataPayload;

public class LightCommands {

    public static void registerCommands() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                CommandManager.literal("lightprofile")
                    .requires(source -> source.hasPermissionLevel(2)) // OP level 2 required
                    .then(CommandManager.argument("action", StringArgumentType.word())
                        .suggests((CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) -> {
                            builder.suggest("save");
                            builder.suggest("load");
//                            builder.suggest("list");
                            return builder.buildFuture();
                        })
                            .then(CommandManager.argument("profile_name", StringArgumentType.word())
                                .executes(context -> {
                                    // Extract the argument values
                                    String action = StringArgumentType.getString(context, "action");
                                    String profileName = StringArgumentType.getString(context, "profile_name");

                                    // Run Command Logic
                                    if (action.equals("save")) {
                                        ClientPlayNetworking.send(new SaveLightsDataPayload(profileName));
                                    } else if (action.equals("load")) {
                                        ClientPlayNetworking.send(new LoadLightsDataPayload(profileName));
                                    }
                                    return 1;
                                }))
                    )
            );
        });
    }
}