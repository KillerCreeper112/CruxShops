package killercreepr.cruxshops.api.shop.trade;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TraderTrade {
    @Nullable ShopTrade getBuyingTrade();
    @Nullable ShopTrade getSellingTrade();

    @Contract(pure = true)
    TraderTrade withBuyingTrade(@NotNull ShopTrade buyingTrade);
    @Contract(pure = true)
    TraderTrade withSellingTrade(@NotNull ShopTrade sellingTrade);
    @Contract(pure = true)
    TraderTrade withTrades(@NotNull ShopTrade buyingTrade, @NotNull ShopTrade sellingTrade);
}
