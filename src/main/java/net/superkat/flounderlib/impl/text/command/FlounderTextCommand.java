package net.superkat.flounderlib.impl.text.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.superkat.flounderlib.FlounderLib;
import net.superkat.flounderlib.api.text.v1.FlounderTextApi;
import net.superkat.flounderlib.api.text.v1.text.FlounderText;
import net.superkat.flounderlib.impl.text.registry.FlounderTextRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FlounderTextCommand {

    public static final SuggestionProvider<CommandSourceStack> AVAILABLE_TEXT_IDS = SuggestionProviders.register(
            Identifier.fromNamespaceAndPath(FlounderLib.MOD_ID, "available_flounder_text_ids"),
            (commandContext, suggestionsBuilder) -> {
                Set<Identifier> idSet = FlounderTextRegistry.TEXT_TYPE_REGISTRY.keySet();
                return SharedSuggestionProvider.suggestResource(idSet, suggestionsBuilder);
            }
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(
                Commands.literal("floundertext")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.argument("text", FlounderTextArgument.flounderText(registryAccess))
                                        .suggests(AVAILABLE_TEXT_IDS)
                                        .executes(context -> executeText(
                                                context.getSource(),
                                                FlounderTextArgument.getFlounderText(context, "text"))
                                        ).then(Commands.argument("viewers", EntityArgument.players())
                                                        .executes(context -> executeText(
                                                                context.getSource(),
                                                                FlounderTextArgument.getFlounderText(context, "text"),
                                                                EntityArgument.getPlayers(context, "viewers"))
                                                        )
                                        )
                        )
        );
    }

    public static int executeText(CommandSourceStack source, FlounderText text) {
        List<ServerPlayer> players = source.getPlayer() == null ? List.of() : List.of(source.getPlayer());
        return executeText(source, text, players);
    }

    public static int executeText(CommandSourceStack source, FlounderText text, Collection<ServerPlayer> viewers) {
        if(viewers.isEmpty()) {
            source.sendSuccess(() -> Component.literal("Did not send any text - there were no viewers to view it!").withStyle(ChatFormatting.RED), false);
            return 0;
        }

        Identifier id = text.getFlounderTextType().id();

        FlounderTextApi.send(text, viewers);
        Component idText = Component.literal(id.toString()).withStyle(ChatFormatting.AQUA);
        String players = viewers.size() > 1 ? "players" : "player";
        Component playerCount = Component.literal(viewers.size() + " " + players).withStyle(ChatFormatting.DARK_AQUA);
        Component feedback = Component.literal("Sent ").append(idText).append(" to ").append(playerCount).append("!");
        source.sendSuccess(() -> feedback, false);
        return 1;
    }

}
