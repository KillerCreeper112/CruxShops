package killercreepr.cruxshops.api.database;

import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;

public interface ShopDatabase {
  void onTradeUse(ShopTrader trader, ShopTrade trade);
}
