package killercreepr.cruxshops.api.component;

import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopTraderComponent {
    default @Nullable TraderTrade adjustTrade(@NotNull ShopTrader trader, @NotNull TraderTrade trade){ return null; }
}
