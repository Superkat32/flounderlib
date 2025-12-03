package net.superkat.flounderlib.impl.text.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.FlounderTextApi;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.registry.FlounderTextRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FlounderTextCommand {

    public static final SuggestionProvider<ServerCommandSource> AVAILABLE_TEXT_IDS = SuggestionProviders.register(
            Identifier.of(FlounderLib.MOD_ID, "available_flounder_text_ids"),
            (commandContext, suggestionsBuilder) -> {
                Set<Identifier> idSet = FlounderTextRegistry.TEXT_TYPE_REGISTRY.getIds();
                return CommandSource.suggestIdentifiers(idSet, suggestionsBuilder);
            }
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("floundertext")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
//                                CommandManager.argument("type", IdentifierArgumentType.identifier())
//                                        .suggests(AVAILABLE_TEXT_IDS)
//                                        .then(
                                            CommandManager.argument("text", FlounderTextArgument.flounderText(registryAccess))
                                                    .suggests(AVAILABLE_TEXT_IDS)
                                                    .executes(context -> executeText(
                                                            context.getSource(),
//                                                            IdentifierArgumentType.getIdentifier(context, "type"),
                                                            FlounderTextArgument.getFlounderText(context, "text"))
                                                    ).then(CommandManager.argument("viewers", EntityArgumentType.players())
                                                                    .executes(context -> executeText(
                                                                            context.getSource(),
//                                                                            IdentifierArgumentType.getIdentifier(context, "type"),
                                                                            FlounderTextArgument.getFlounderText(context, "text"),
                                                                            EntityArgumentType.getPlayers(context, "viewers"))
                                                                    )
                                                    )
//                                        )
                        )
        );
    }

    public static int executeText(ServerCommandSource source, FlounderText text) {
        List<ServerPlayerEntity> players = source.getPlayer() == null ? List.of() : List.of(source.getPlayer());
        return executeText(source, text, players);
    }

    public static int executeText(ServerCommandSource source, FlounderText text, Collection<ServerPlayerEntity> viewers) {
        if(viewers.isEmpty()) {
            source.sendFeedback(() -> Text.literal("Did not send any text - there were no viewers to view it!").formatted(Formatting.RED), false);
            return 0;
        }

        Identifier id = text.getId();

        FlounderTextApi.sendText(viewers, id, text);
        Text idText = Text.literal(id.toString()).formatted(Formatting.AQUA);
        String players = viewers.size() > 1 ? "players" : "player";
        Text playerCount = Text.literal(viewers.size() + " " + players).formatted(Formatting.DARK_AQUA);
        Text feedback = Text.literal("Sent ").append(idText).append(" to ").append(playerCount).append("!");
        source.sendFeedback(() -> feedback, false);
        return 1;
    }

}
