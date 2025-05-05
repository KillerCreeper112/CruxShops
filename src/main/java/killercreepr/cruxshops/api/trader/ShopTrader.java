package killercreepr.cruxshops.api.trader;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ShopTrader extends TraderTradesHolder, DataComponentAccessor {
    @NotNull TraderProfession getProfession();
    void setTrades(@NotNull List<TraderTrade> trades);

    boolean purchaseTrade(@NotNull Entity e, @NotNull ShopTrade trade);

    @NotNull CanPurchase canPurchaseTrade(@NotNull Entity p, @NotNull ShopTrade trade);

    MergedTagContainer buildTags(ShopTrade trade);
    enum CanPurchase{
        TRUE,
        CANNOT_AFFORD,
        CANNOT_ACCEPT,
        NOT_ENOUGH_STOCK
    }
}
