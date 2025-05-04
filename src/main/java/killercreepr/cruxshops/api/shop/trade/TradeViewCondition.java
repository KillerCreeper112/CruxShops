package killercreepr.cruxshops.api.shop.trade;

import org.bukkit.entity.Entity;

public interface TradeViewCondition {
    boolean canView(Entity e);
}
