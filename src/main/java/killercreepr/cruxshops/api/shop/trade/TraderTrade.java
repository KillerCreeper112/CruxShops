package killercreepr.cruxshops.api.shop.trade;

import killercreepr.crux.api.component.DataComponentAccessor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface TraderTrade extends DataComponentAccessor {
    @Nullable ShopTrade getBuyingTrade();
    @Nullable ShopTrade getSellingTrade();

    @Contract(pure = true)
    TraderTrade withBuyingTrade(@Nullable ShopTrade buyingTrade);
    @Contract(pure = true)
    TraderTrade withSellingTrade(@Nullable ShopTrade sellingTrade);
    @Contract(pure = true)
    TraderTrade withTrades(@Nullable ShopTrade buyingTrade, @Nullable ShopTrade sellingTrade);
}
