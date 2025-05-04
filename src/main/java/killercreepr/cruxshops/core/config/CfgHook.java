package killercreepr.cruxshops.core.config;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.config.handler.*;

public class CfgHook {
    public static final FileShopTrade SHOP_TRADE = new FileShopTrade();
    public static final FileShopTradeIngredient SHOP_TRADE_INGREDIENT = new FileShopTradeIngredient();
    public static final FileShopTradeResult SHOP_TRADE_RESULT = new FileShopTradeResult();
    public static final FileShopTrader SHOP_TRADER = new FileShopTrader();
    public static final FileTraderTrade TRADER_TRADE = new FileTraderTrade();

    public static void load(){
        CfgRegistries.SIMPLE_REGISTRY.forEach(CfgHook::registerHandlers);
    }

    public static void registerHandlers(FileRegistry reg){
        reg.registerFileHandler(ShopTrade.class, SHOP_TRADE);
        reg.registerFileHandler(ShopTradeIngredient.class, SHOP_TRADE_INGREDIENT);
        reg.registerFileHandler(ShopTradeResult.class, SHOP_TRADE_RESULT);
        reg.registerFileHandler(ShopTrader.class, SHOP_TRADER);
        reg.registerFileHandler(TraderTrade.class, TRADER_TRADE);
    }

}
