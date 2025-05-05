package killercreepr.cruxshops.core.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.component.parser.DataComponentDecoder;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.base.parsed.FileParsedObjectHandler;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxcurrency.core.registry.CruxCurrencyRegistries;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import killercreepr.cruxmenus.core.menu.holder.SimpleMenuHolder;
import killercreepr.cruxshops.api.config.handler.FileObjectHandlerKeyed;
import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.config.handler.*;
import killercreepr.cruxshops.core.config.loader.CfgShopTraderLoader;
import killercreepr.cruxshops.core.menu.ShopTraderMenu;
import killercreepr.cruxshops.core.profession.SimpleTraderProfession;
import killercreepr.cruxshops.core.shop.SimpleShopTrade;
import killercreepr.cruxshops.core.shop.trade.SimpleTraderTrade;
import killercreepr.cruxshops.core.shop.trade.ingredient.CruxCurrencyTradeIngredient;
import killercreepr.cruxshops.core.shop.trade.ingredient.PluginItemTradeIngredient;
import killercreepr.cruxshops.core.shop.trade.result.CruxCurrencyTradeResult;
import killercreepr.cruxshops.core.shop.trade.result.PluginItemTradeResult;
import killercreepr.cruxshops.core.trader.SimpleShopTrader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class CfgHook {
    public static final FileShopTrade SHOP_TRADE = new FileShopTrade();
    public static final FileShopTradeIngredient SHOP_TRADE_INGREDIENT = new FileShopTradeIngredient();
    public static final FileShopTradeResult SHOP_TRADE_RESULT = new FileShopTradeResult();
    public static final FileShopTrader SHOP_TRADER = new FileShopTrader();
    public static final FileTraderTrade TRADER_TRADE = new FileTraderTrade();
    public static final FileTraderProfession TRADER_PROFESSION = new FileTraderProfession();

    public static void load(){
        registerShopTradeTypes();
        registerShopIngredientTypes();
        registerTradeResultTypes();
        registerTraderTradeTypes();
        registerShopTraderTypes();
        registerTraderProfessionTypes();
        CfgRegistries.SIMPLE_REGISTRY.forEach(reg ->{
            registerHandlers(reg);
            reg.getParsedObjectRegistry().register(new FileParsedObjectHandler<MenuHolder>() {
                @Override
                public @NotNull Key key() {
                    return Crux.key("cruxshops/menus");
                }

                @Override
                public int getPriority() {
                    return 2;
                }

                @Override
                public @NotNull Class<MenuHolder> getTargetType() {
                    return MenuHolder.class;
                }

                @Override
                public @Nullable MenuHolder parse(@NotNull FileElement element, @NotNull FileContext<?> ctx,
                                                  @NotNull MenuHolder original, @Nullable MenuHolder current) {
                    if(current==null) return null;
                    String shopType = current.info().get("cruxshops/menu_type", String.class);
                    if(shopType == null) return current;
                    switch (shopType.toLowerCase()){
                        case "simple" ->{
                            return new SimpleMenuHolder(current.key(), current.getTitle(), current.getSize(), current.getItems(), current.info(), current.getModules()){
                                @Override
                                public @NotNull ConfigMenu createMenu(@NotNull DataExchange data, @Nullable MergedTagContainer tags) {
                                    return new ShopTraderMenu(this, data);
                                }
                            };
                        }
                    }
                    return current;
                }
            });
        });
    }

    public static void reload(CruxPlugin plugin){
        new CfgShopTraderLoader(SHOP_TRADER).loadConfiguration(
            new CruxFolder(plugin, "shops").file()
        );
    }

    public static void registerHandlers(FileRegistry reg){
        reg.registerFileHandler(ShopTrade.class, SHOP_TRADE);
        reg.registerFileHandler(ShopTradeIngredient.class, SHOP_TRADE_INGREDIENT);
        reg.registerFileHandler(ShopTradeResult.class, SHOP_TRADE_RESULT);
        reg.registerFileHandler(ShopTrader.class, SHOP_TRADER);
        reg.registerFileHandler(TraderTrade.class, TRADER_TRADE);
        reg.registerFileHandler(TraderProfession.class, TRADER_PROFESSION);
    }

    public static void registerTraderProfessionTypes(){
        TRADER_PROFESSION.registerType(Crux.key("simple"), new FileObjectHandler<TraderProfession>() {
            @Nullable
            @Override
            public TraderProfession deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();

                List<TraderTrade> trades = reg.deserializeFromFile(
                    new TypeToken<List<TraderTrade>>(){}.getType(), o.get("trades")
                );
                if(trades == null) return null;

                return new SimpleTraderProfession(trades);
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TraderProfession trade) {
                return null;
            }
        });
    }

    public static void registerShopTraderTypes(){
        SHOP_TRADER.registerType(Crux.key("simple"), new FileObjectHandlerKeyed<>() {
            @Nullable
            @Override
            public ShopTrader deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();

                TraderProfession profession = reg.deserializeFromFile(TraderProfession.class, o.get("profession"));
                if(profession == null) return null;
                Collection<TypedDataComponent<?>> components = null;
                String componentText = reg.deserializeFromFile(String.class, o.get("components"));
                if(componentText != null) components = DataComponentDecoder.componentDecoder().parseComponents(componentText);
                return new SimpleShopTrader(key, profession, components);
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull ShopTrader trade) {
                return null;
            }
        });
    }

    public static void registerTraderTradeTypes(){
        TRADER_TRADE.registerType(Crux.key("simple"), new FileObjectHandler<>() {
            @Nullable
            @Override
            public TraderTrade deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();

                ShopTrade buying = reg.deserializeFromFile(ShopTrade.class, o.get("buying_trade"));
                ShopTrade selling = reg.deserializeFromFile(ShopTrade.class, o.get("selling_trade"));
                if(buying == null && selling == null) return null;
                Collection<TypedDataComponent<?>> components = null;
                String componentText = reg.deserializeFromFile(String.class, o.get("components"));
                if(componentText != null) components = DataComponentDecoder.componentDecoder().parseComponents(componentText);
                return new SimpleTraderTrade(buying, selling, components);
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TraderTrade trade) {
                return null;
            }
        });
        TRADER_TRADE.registerType(Crux.key("generic_flip"), new FileObjectHandler<>() {
            @Nullable
            @Override
            public TraderTrade deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();
                String itemType = reg.deserializeFromFile(String.class, o.get("result"));
                if(itemType == null) return null;
                int sell = reg.deserializeFromFile(Integer.class, o.get("sell"));
                int buy = reg.deserializeFromFile(Integer.class, o.get("buy"));
                Key currency = reg.deserializeFromFile(Key.class, o.get("currency"));
                if(currency == null) currency = Crux.key("orbit");

                String[] args = itemType.split(" ");
                Key finalCurrency = currency;
                ShopTrade buying = new SimpleShopTrade(
                    List.of(new CruxCurrencyTradeIngredient(() -> CruxCurrencyRegistries.CURRENCY.get(finalCurrency), buy, null)),
                    List.of(new PluginItemTradeResult(Crux.handlers().item().getItem(Crux.key(args[0])),
                        args.length < 2 ? 1 : Integer.parseInt(args[1]), null)),
                    null, null
                );

                ShopTrade selling = new SimpleShopTrade(
                    List.of(new PluginItemTradeIngredient(Crux.handlers().item().getItem(Crux.key(args[0])),
                        args.length < 3 ? (args.length < 2 ? 1 : Integer.parseInt(args[1])) : Integer.parseInt(args[2]), null)),
                    List.of(new CruxCurrencyTradeResult(() -> CruxCurrencyRegistries.CURRENCY.get(finalCurrency), sell, null)),
                    null, null
                );

                Collection<TypedDataComponent<?>> components = null;
                String componentText = reg.deserializeFromFile(String.class, o.get("components"));
                if(componentText != null) components = DataComponentDecoder.componentDecoder().parseComponents(componentText);
                return new SimpleTraderTrade(buying, selling, components);
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TraderTrade trade) {
                return null;
            }
        });
    }

    public static void registerShopTradeTypes(){
        SHOP_TRADE.registerType(Crux.key("simple"), new FileObjectHandler<>() {
            @Nullable
            @Override
            public ShopTrade deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();
                List<ShopTradeIngredient> ingredients = reg.deserializeFromFile(new TypeToken<List<ShopTradeIngredient>>(){}.getType(), o.get("ingredients"));
                List<ShopTradeResult> results = reg.deserializeFromFile(new TypeToken<List<ShopTradeResult>>(){}.getType(), o.get("results"));
                if(ingredients == null || results == null) return null;
                LootCondition condition = reg.deserializeFromFile(LootCondition.class, o.get("view_condition"));
                return new SimpleShopTrade(
                    ingredients, results, condition, null
                );
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull ShopTrade trade) {
                return null;
            }
        });
    }

    public static void registerShopIngredientTypes(){
        SHOP_TRADE_INGREDIENT.registerType(Crux.key("item"), new FileObjectHandler<>() {
            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> fileContext, @NotNull ShopTradeIngredient trade) {
                return null;
            }

            @Nullable
            @Override
            public ShopTradeIngredient deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();
                ItemHolder holder = reg.deserializeFromFile(ItemHolder.class, o.get("item"));
                Integer amount = reg.deserializeFromFile(Integer.class, o.get("amount"));
                if(amount == null) amount = 1;
                return new PluginItemTradeIngredient(holder, amount, null);
            }
        });
        SHOP_TRADE_INGREDIENT.registerType(Crux.key("crux_currency"), new FileObjectHandler<>() {
            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> fileContext, @NotNull ShopTradeIngredient trade) {
                return null;
            }

            @Nullable
            @Override
            public ShopTradeIngredient deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();
                Key key = reg.deserializeFromFile(Key.class, o.get("currency"));
                Integer amount = reg.deserializeFromFile(Integer.class, o.get("amount"));
                if(amount == null) amount = 1;
                return new CruxCurrencyTradeIngredient(() -> CruxCurrencyRegistries.CURRENCY.get(key), amount, null);
            }
        });
    }

    public static void registerTradeResultTypes(){
        SHOP_TRADE_RESULT.registerType(Crux.key("item"), new FileObjectHandler<>() {
            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> fileContext, @NotNull ShopTradeResult trade) {
                return null;
            }

            @Nullable
            @Override
            public ShopTradeResult deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();
                ItemHolder holder = reg.deserializeFromFile(ItemHolder.class, o.get("item"));
                Integer amount = reg.deserializeFromFile(Integer.class, o.get("amount"));
                if(amount == null) amount = 1;
                return new PluginItemTradeResult(holder, amount, null);
            }
        });
        SHOP_TRADE_RESULT.registerType(Crux.key("crux_currency"), new FileObjectHandler<>() {
            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> fileContext, @NotNull ShopTradeResult trade) {
                return null;
            }

            @Nullable
            @Override
            public ShopTradeResult deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var reg = ctx.getRegistry();
                Key key = reg.deserializeFromFile(Key.class, o.get("currency"));
                Integer amount = reg.deserializeFromFile(Integer.class, o.get("amount"));
                if(amount == null) amount = 1;
                return new CruxCurrencyTradeResult(() -> CruxCurrencyRegistries.CURRENCY.get(key), amount, null);
            }
        });
    }
}
