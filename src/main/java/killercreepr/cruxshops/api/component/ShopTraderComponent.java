package killercreepr.cruxshops.api.component;

import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopTraderComponent {
    default @Nullable TraderTrade adjustTrade(@NotNull ShopTrader trader, @NotNull Entity viewer, @NotNull TraderTrade trade){ return null; }
    default void onTradePurchased(@NotNull ShopTrader trader, @NotNull Entity e, @NotNull TraderTrade traderTrade, @NotNull ShopTrade trade){

    }
    default @Nullable MergedTagContainer buildTags(@NotNull ShopTrader trader, @NotNull ShopTrade trade){
        return null;
    }

    default void save(@NotNull FileContext<?> ctx, @NotNull FileObject object, @NotNull ShopTrader trader){
    }
    default void load(@NotNull FileContext<?> ctx, @NotNull FileObject object, @NotNull ShopTrader trader){
    }
    default void tick(int tick, int delay, @NotNull ShopTrader trader){

    }
}
