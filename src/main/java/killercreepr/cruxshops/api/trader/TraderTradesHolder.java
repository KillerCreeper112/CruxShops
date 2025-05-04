package killercreepr.cruxshops.api.trader;

import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TraderTradesHolder {
    @NotNull List<TraderTrade> getTrades(@NotNull Entity viewer);
}
