package killercreepr.cruxshops.api.component;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TraderTradeComponent {
    @Contract(pure = true)
    TraderTradeComponent createCopy();

    default @Nullable FileElement save(@NotNull FileContext<?> ctx, @NotNull ShopTrader trader, @NotNull TraderTrade trade){
        return null;
    }
}
