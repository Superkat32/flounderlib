package net.superkat.flounderlib.command.text;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.type.FlounderTextParams;
import net.superkat.flounderlib.network.text.FlounderTextS2CPacket;
import net.superkat.flounderlib.text.client.FlounderClientTextManager;

import java.util.Set;

public class FlounderTextCommand {


    public static final SuggestionProvider<ServerCommandSource> AVAILABLE_TEXT_TYPES = SuggestionProviders.register(
            Identifier.of(FlounderLib.MOD_ID, "available_text_ids"),
            (commandContext, suggestionsBuilder) -> {
                Set<Identifier> idSet = FlounderClientTextManager.getRegistry().getIds();
                return CommandSource.suggestIdentifiers(idSet, suggestionsBuilder);
            }
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("floundertext")
                        .requires(source -> source.hasPermissionLevel(3))
                        .then(
                                CommandManager.argument("type", IdentifierArgumentType.identifier())
                                        .suggests(AVAILABLE_TEXT_TYPES)
                                        .then(
                                                CommandManager.argument("text", TextArgumentType.text(registryAccess))
                                                        .executes(context ->
                                                                executeText(
                                                                        context.getSource(),
                                                                        IdentifierArgumentType.getIdentifier(context, "type"),
                                                                        TextArgumentType.getTextArgument(context, "text")
                                                                )
                                                        )
                                        )
                                        .then(
                                                CommandManager.argument("type", FlounderTextParamsArgument.flounderTextParams(registryAccess))
                                                        .executes(context ->
                                                                executeText(
                                                                        context.getSource(),
                                                                        IdentifierArgumentType.getIdentifier(context, "type"),
                                                                        FlounderTextParamsArgument.getFlounderTextParams(context, "type")
                                                                )
                                                        )
                                        )
                        )
        );
    }


    public static int executeText(ServerCommandSource source, Identifier identifier, Text text) {
        FlounderTextParams params = new FlounderTextParams.Default(text);
        return executeText(source, identifier, params);
    }

    public static int executeText(ServerCommandSource source, Identifier identifier, FlounderTextParams params) {
        ServerPlayerEntity player = source.getPlayer();

        ServerPlayNetworking.send(player, new FlounderTextS2CPacket<>(identifier, params));
        Text id = Text.literal(identifier.toString()).formatted(Formatting.AQUA);
        Text feedback = Text.literal("Displaying Text: ").append(id).append("!");
        source.sendFeedback(() -> feedback, false);
        return 1;
    }


}
