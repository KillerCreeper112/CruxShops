package killercreepr.cruxshops.core.shop.trade;

import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.jetbrains.annotations.NotNull;
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

    @Override
    public TraderTrade withBuyingTrade(@NotNull ShopTrade buyingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade);
    }

    @Override
    public TraderTrade withSellingTrade(@NotNull ShopTrade sellingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade);
    }

    @Override
    public TraderTrade withTrades(@NotNull ShopTrade buyingTrade, @NotNull ShopTrade sellingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade);
    }
}
