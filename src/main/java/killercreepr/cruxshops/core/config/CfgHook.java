package killercreepr.cruxshops.core.config;

import killercreepr.crux.core.Crux;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxcurrency.core.registry.CruxCurrencyRegistries;
import killercreepr.cruxshops.api.config.handler.FileObjectHandlerKeyed;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.config.handler.*;
import killercreepr.cruxshops.core.shop.trade.ingredient.CruxCurrencyTradeIngredient;
import killercreepr.cruxshops.core.shop.trade.ingredient.PluginItemTradeIngredient;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgHook {
    public static final FileShopTrade SHOP_TRADE = new FileShopTrade();
    public static final FileShopTradeIngredient SHOP_TRADE_INGREDIENT = new FileShopTradeIngredient();
    public static final FileShopTradeResult SHOP_TRADE_RESULT = new FileShopTradeResult();
    public static final FileShopTrader SHOP_TRADER = new FileShopTrader();
    public static final FileTraderTrade TRADER_TRADE = new FileTraderTrade();

    public static void load(){
        registerShopTradeTypes();
        registerShopIngredientTypes();
        CfgRegistries.SIMPLE_REGISTRY.forEach(CfgHook::registerHandlers);
    }

    public static void registerHandlers(FileRegistry reg){
        reg.registerFileHandler(ShopTrade.class, SHOP_TRADE);
        reg.registerFileHandler(ShopTradeIngredient.class, SHOP_TRADE_INGREDIENT);
        reg.registerFileHandler(ShopTradeResult.class, SHOP_TRADE_RESULT);
        reg.registerFileHandler(ShopTrader.class, SHOP_TRADER);
        reg.registerFileHandler(TraderTrade.class, TRADER_TRADE);
    }

    public static void registerShopTradeTypes(){
        SHOP_TRADE.registerType(Crux.key("simple"), new FileObjectHandlerKeyed<>() {
            @Nullable
            @Override
            public ShopTrade deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
                return null;
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> fileContext, @NotNull ShopTrade trade) {
                return null;
            }

            @Nullable
            @Override
            public ShopTrade deserializeFromFile(@NotNull FileContext<?> fileContext, @NotNull FileElement fileElement) {
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
                int amount = reg.deserializeFromFile(Integer.class, o.get("amount"));
                return new PluginItemTradeIngredient(holder, amount);
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
                int amount = reg.deserializeFromFile(Integer.class, o.get("amount"));
                return new CruxCurrencyTradeIngredient(() -> CruxCurrencyRegistries.CURRENCY.get(key), amount);
            }
        });
    }
}
