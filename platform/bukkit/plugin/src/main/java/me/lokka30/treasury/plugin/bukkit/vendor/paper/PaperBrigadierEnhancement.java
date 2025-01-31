/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/lokka30/Treasury>.
 */

package me.lokka30.treasury.plugin.bukkit.vendor.paper;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommand;
import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Locale;
import me.lokka30.treasury.plugin.core.TreasuryPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperBrigadierEnhancement implements Listener {

    @EventHandler
    public void onCommandRegister(CommandRegisteredEvent<BukkitBrigadierCommandSource> event) {
        String label = event.getCommandLabel();
        if (label.equalsIgnoreCase("treasury") || label.equalsIgnoreCase("treasury:treasury")) {
            event.setLiteral(buildLiteral(label, event.getBrigadierCommand()));
        }
    }

    //@formatter:off
    private LiteralCommandNode<BukkitBrigadierCommandSource> buildLiteral(
            String label, BukkitBrigadierCommand<BukkitBrigadierCommandSource> command
    ) {
        return LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal(label)
                .requires(source -> source.getBukkitSender().hasPermission("treasury.command.treasury"))
                .executes(command)
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("help")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.help"))
                                .executes(command)
                )
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("info")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.info"))
                                .executes(command)
                )
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("reload")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.reload"))
                                .executes(command)
                )
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("downloadLatest")
                                .requires(source -> source.getBukkitSender()
                                        .hasPermission("treasury.command.treasury.downloadLatest"))
                                .executes(command)
                )
                .then(
                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("economy")
                                .requires(source -> source.getBukkitSender().hasPermission("treasury.command.treasury.economy"))
                                .executes(command)
                                .then(
                                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("info")
                                                .requires(source -> source.getBukkitSender().hasPermission("treasury.command" +
                                                        ".treasury.economy.info"))
                                                .executes(command)
                                )
                                .then(
                                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("help")
                                                .requires(source -> source.getBukkitSender().hasPermission("treasury.command" +
                                                        ".treasury.economy.help"))
                                                .executes(command)
                                )
                                .then(
                                        LiteralArgumentBuilder.<BukkitBrigadierCommandSource>literal("migrate")
                                                .requires(source -> source.getBukkitSender()
                                                        .hasPermission("treasury.command.treasury.economy.migrate"))
                                                .executes(command)
                                                .then(
                                                        RequiredArgumentBuilder.<BukkitBrigadierCommandSource, String>argument(
                                                                        "plugin1",
                                                                        StringArgumentType.word()
                                                                )
                                                                .requires(source -> source.getBukkitSender()
                                                                        .hasPermission("treasury.command.treasury.migrate"))
                                                                .suggests(plugins())
                                                                .executes(command)
                                                                .then(
                                                                        RequiredArgumentBuilder
                                                                                .<BukkitBrigadierCommandSource, String>argument(
                                                                                        "plugin2",
                                                                                        StringArgumentType.word()
                                                                                )
                                                                                .requires(source -> source.getBukkitSender()
                                                                                        .hasPermission(
                                                                                                "treasury.command.treasury.migrate"))
                                                                                .suggests(plugins())
                                                                                .executes(command)
                                                                )
                                                )
                                )
                ).build();
    }
    //@formatter:on

    private SuggestionProvider<BukkitBrigadierCommandSource> plugins() {
        return (context, builder) -> {
            String lastArg = builder.getRemaining().toLowerCase(Locale.ROOT);

            for (String pluginRegistering : TreasuryPlugin
                    .getInstance()
                    .pluginsListRegisteringProvider()) {
                if (pluginRegistering.toLowerCase(Locale.ROOT).startsWith(lastArg)) {
                    builder.suggest(pluginRegistering);
                }
            }

            return builder.buildFuture();
        };
    }

}
