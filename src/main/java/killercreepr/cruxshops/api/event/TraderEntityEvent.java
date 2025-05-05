package killercreepr.cruxshops.api.event;

import killercreepr.cruxshops.api.trader.ShopTrader;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class TraderEntityEvent extends EntityEvent {
    protected final @NotNull ShopTrader trader;
    public TraderEntityEvent(@NotNull Entity who, @NotNull ShopTrader trader) {
        super(who);
        this.trader = trader;
    }

    @NotNull
    public ShopTrader getTrader() {
        return trader;
    }
}
