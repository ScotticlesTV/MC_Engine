package net.scotticles.mcengine.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.scotticles.mcengine.networking.lights.payloads.LoadLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.SaveLightsDataPayload;

public class RegionCommands {

    public static void registerCommands() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                CommandManager.literal("region")
                    .requires(source -> source.hasPermissionLevel(2)) // OP level 2 required
                    .then(CommandManager.argument("action", StringArgumentType.word())
                        .suggests((CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) -> {
                            builder.suggest("enable");
                            builder.suggest("disable");
                            return builder.buildFuture();
                        })
                            .then(CommandManager.argument("region_name", StringArgumentType.word())
                                .executes(context -> {
                                    // Extract the argument values
                                    String action = StringArgumentType.getString(context, "action");

                                    // Run Command Logic
                                    if (action.equals("enable")) {
                                        // Enable Region With The Specified Name
                                        // Send A Response Message For If It Succeeded, Including Why Or Why Not
                                    } else if (action.equals("disable")) {
                                        // Disable Region With The Specified Name
                                        // Send A Response Message For If It Succeeded, Including Why Or Why Not
                                    }
                                    return 1;
                                }))
                    )
            );
        });
    }
}