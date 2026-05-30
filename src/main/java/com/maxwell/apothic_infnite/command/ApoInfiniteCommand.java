package com.maxwell.apothic_infnite.command;

import com.maxwell.apothic_infnite.AFP;
import com.maxwell.apothic_infnite.InfiniteConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AFP.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ApoInfiniteCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("apoinf")
                .requires(source -> true)
                .then(Commands.literal("max_sockets")
                        .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    int val = IntegerArgumentType.getInteger(context, "value");
                                    InfiniteConfig.MAX_SOCKETS.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "max_sockets", val), true);
                                    return 1;
                                })))
                .then(Commands.literal("max_affix_level")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(1.0))
                                .executes(context -> {
                                    double val = DoubleArgumentType.getDouble(context, "value");
                                    InfiniteConfig.MAX_AFFIX_LEVEL.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "max_affix_level", val), true);
                                    return 1;
                                })))
                .then(Commands.literal("min_affix_level")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0))
                                .executes(context -> {
                                    double val = DoubleArgumentType.getDouble(context, "value");
                                    InfiniteConfig.MIN_AFFIX_LEVEL.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "min_affix_level", val), true);
                                    return 1;
                                })))
                .then(Commands.literal("enable_all_items_reforge")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean val = BoolArgumentType.getBool(context, "value");
                                    InfiniteConfig.ENABLE_ALL_ITEMS_REFORGE.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "enable_all_items_reforge", val), true);
                                    return 1;
                                })))
                .then(Commands.literal("max_enchantment_level")
                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 255))
                                .executes(context -> {
                                    int val = IntegerArgumentType.getInteger(context, "value");
                                    InfiniteConfig.MAX_ENCHANTMENT_LEVEL.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "max_enchantment_level", val), true);
                                    return 1;
                                })))

                .then(Commands.literal("enable_infinite_enchanting")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean val = BoolArgumentType.getBool(context, "value");
                                    InfiniteConfig.ENABLE_INFINITE_ENCHANTING.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "enable_infinite_enchanting", val), true);
                                    return 1;
                                })))
                .then(Commands.literal("allow_high_level_curses")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean val = BoolArgumentType.getBool(context, "value");
                                    InfiniteConfig.ALLOW_HIGH_LEVEL_CURSES.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "allow_high_level_curses", val), true);
                                    return 1;
                                })))
                .then(Commands.literal("enable_world_destruction")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean val = BoolArgumentType.getBool(context, "value");
                                    InfiniteConfig.ENABLE_WORLD_DESTRUCTION.set(val);
                                    InfiniteConfig.SPEC.save();
                                    context.getSource().sendSuccess(() -> Component.translatable("message.apothic_infnite.config_changed", "enable_world_destruction", val), true);
                                    return 1;
                                })))
        );
    }
}