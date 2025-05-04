package killercreepr.cruxshops.core.shop.trade;

import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.jetbrains.annotations.Nullable;

public class SimpleTraderTrade implements TraderTrade {
    protected final ShopTrade buyingTrade;
    protected final ShopTrade sellingTrade;

    public SimpleTraderTrade(ShopTrade buyingTrade, ShopTrade sellingTrade) {
        this.buyingTrade = buyingTrade;
        this.sellingTrade = sellingTrade;
    }

    @Nullable
    @Override
    public ShopTrade getBuyingTrade() {
        return buyingTrade;
    }

    @Nullable
    @Override
    public ShopTrade getSellingTrade() {
        return sellingTrade;
    }
}
