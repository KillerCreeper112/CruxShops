package killercreepr.cruxshops.api.event;

import killercreepr.cruxshops.api.trader.ShopTrader;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public abstract class TraderEntityCancelEvent extends TraderEntityEvent implements Cancellable {
    protected boolean cancel = false;
    public TraderEntityCancelEvent(@NotNull Entity who, @NotNull ShopTrader trader) {
        super(who, trader);
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
