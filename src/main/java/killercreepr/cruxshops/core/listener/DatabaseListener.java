package killercreepr.cruxshops.core.listener;

import killercreepr.cruxshops.api.database.ShopDatabase;
import killercreepr.cruxshops.api.event.EntityPurchaseTraderTradeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class DatabaseListener implements Listener {
  public final ShopDatabase database;

  public DatabaseListener(ShopDatabase database) {
    this.database = database;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEntityPurchaseTraderTrade(EntityPurchaseTraderTradeEvent event) {
    database.onTradeUse(event.getTrader(), event.getTrade());
  }

}
