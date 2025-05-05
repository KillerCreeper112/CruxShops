package killercreepr.cruxshops.api.event;

import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EntityPurchaseTraderTradeEvent extends TraderEntityCancelEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected @NotNull ShopTrade trade;
    public EntityPurchaseTraderTradeEvent(@NotNull Entity who, @NotNull ShopTrader trader, @NotNull ShopTrade trade) {
        super(who, trader);
        this.trade = trade;
    }

    @NotNull
    public ShopTrade getTrade() {
        return trade;
    }

    public void setTrade(@NotNull ShopTrade trade) {
        this.trade = trade;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
