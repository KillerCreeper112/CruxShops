package killercreepr.cruxshops.api.component;

import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopTraderComponent {
    default @Nullable TraderTrade adjustTrade(@NotNull ShopTrader trader, @NotNull TraderTrade trade){ return null; }
    default void onTradePurchased(@NotNull ShopTrader trader, @NotNull Entity e, @NotNull ShopTrade trade){

    }
    default @Nullable MergedTagContainer buildTags(@NotNull ShopTrader trader, @NotNull ShopTrade trade){
        return null;
    }
}
