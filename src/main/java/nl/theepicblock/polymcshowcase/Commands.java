package nl.theepicblock.polymcshowcase;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.lwjgl.system.CallbackI;

public class Commands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal("polymc-showcase")
                .then(commandWithOptionalTarget("query", (context, target) -> {
                    if (((PlayerDuck)target).getIsUsingPolyMc()) {
                        context.getSource().sendFeedback(new LiteralText("PolyMc is enabled for ").append(target.getDisplayName()), false);
                    } else {
                        context.getSource().sendFeedback(new LiteralText("PolyMc is disabled for ").append(target.getDisplayName()), false);
                    }
                    return Command.SINGLE_SUCCESS;
                }))
                .then(commandWithOptionalTarget("enable", (context, target) -> {
                    PolyMcShowcase.setPolyMcEnabled(target, true);
                    return Command.SINGLE_SUCCESS;
                }))
                .then(commandWithOptionalTarget("disable", (context, target) -> {
                    PolyMcShowcase.setPolyMcEnabled(target, false);
                    return Command.SINGLE_SUCCESS;
                }))
        );
    }

    public static LiteralArgumentBuilder<ServerCommandSource> commandWithOptionalTarget(String name, FunctionalInterfaceThingy executor) {
        return CommandManager.literal(name)
                .executes(context -> executor.execute(context, context.getSource().getPlayer()))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                        .requires(source -> source.hasPermissionLevel(3))
                        .executes(context -> {
                            for (ServerPlayerEntity target : EntityArgumentType.getPlayers(context, "target")) {
                                executor.execute(context, target);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                );
    }


    public interface FunctionalInterfaceThingy {
        int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity target);
    }
}
