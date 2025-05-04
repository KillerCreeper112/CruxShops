package killercreepr.cruxshops.api.shop.trade;

import org.jetbrains.annotations.Nullable;

public interface TraderTrade {
    @Nullable ShopTrade getBuyingTrade();
    @Nullable ShopTrade getSellingTrade();
}
