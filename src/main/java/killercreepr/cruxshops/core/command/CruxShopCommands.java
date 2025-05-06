package killercreepr.cruxshops.core.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.core.Crux;
import killercreepr.cruxcore.CruxCore;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.CruxShopsPlugin;
import killercreepr.cruxshops.core.command.argument.ShopArguments;
import killercreepr.cruxshops.core.market.SimpleMarket;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CruxShopCommands {
    protected final CruxShopsPlugin plugin;

    public CruxShopCommands(CruxShopsPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxshop")
                    .requires(source -> source.getSender().hasPermission("cruxshops.cmds.cruxshop.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cshop"));
        });
    }

    public LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> manager){
        dispatcher.then(
            Commands.literal("shop")
                .then(
                    Commands.argument("trader", ShopArguments.SHOP_TRADER)
                        .executes(ctx ->{
                            if(!(getExecutor(ctx.getSource()) instanceof Player p )) return -1;
                            ShopTrader merchant = ctx.getArgument("trader", ShopTrader.class);
                            CruxCore.inst().cruxMenus().menuRegistry().menuHolders().get(Crux.key("shop_trader/default"))
                                .open(p, DataExchange.builder().put(merchant).build());
                            return 1;
                        })
                        .then(
                            Commands.argument("targets", ArgumentTypes.players())
                                .requires(source -> source.getSender().hasPermission("cruxshops.cmds.cruxshop.target_others"))
                                .executes(ctx ->{
                                    ShopTrader merchant = ctx.getArgument("trader", ShopTrader.class);
                                    ctx.getArgument("targets", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource())
                                            .forEach(p ->{
                                                CruxCore.inst().cruxMenus().menuRegistry().menuHolders().get(Crux.key("shop_trader/default"))
                                                    .open(p, DataExchange.builder().put(merchant).build());
                                            });
                                    return 1;
                                })
                                .then(
                                    Commands.argument("menu", ArgumentTypes.key())
                                        .executes(ctx ->{
                                            ShopTrader merchant = ctx.getArgument("trader", ShopTrader.class);
                                            Key menu = ctx.getArgument("menu", Key.class);
                                            ctx.getArgument("targets", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource())
                                                .forEach(p ->{
                                                    CruxCore.inst().cruxMenus().menuRegistry().menuHolders().get(menu)
                                                        .open(p, DataExchange.builder().put(merchant).build());
                                                });
                                            return 1;
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("tick")
                .then(
                    Commands.argument("step", IntegerArgumentType.integer())
                        .executes(ctx ->{
                            var sender = getExecutor(ctx.getSource());
                            var market = plugin.getGlobalMarket();
                            if(!(market instanceof SimpleMarket m)) return -1;
                            int step = ctx.getArgument("step", Integer.class);
                            for(int i = 0; i < step; i++){
                                m.tick();
                            }
                            sender.sendMessage("Ticked market " + step + " times");
                            return 1;
                        })
                )
        )
        ;
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }
}
