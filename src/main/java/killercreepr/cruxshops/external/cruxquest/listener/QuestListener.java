package killercreepr.cruxshops.external.cruxquest.listener;

import killercreepr.cruxquest.core.listener.AbstractQuestListener;
import killercreepr.cruxshops.api.event.EntityPurchaseTraderTradeEvent;
import killercreepr.cruxshops.external.cruxquest.quest.objective.PurchaseTraderTradeQuestObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class QuestListener extends AbstractQuestListener implements Listener {
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEntityPurchaseTraderTrade(EntityPurchaseTraderTradeEvent event) {
    if(!(event.getEntity() instanceof Player p)) return;
    forEachTracker(p, tracker ->{
      tracker.applyObjectives(PurchaseTraderTradeQuestObjective.class, (parent, child) ->{
        parent.trigger(child, event);
      });
    });
  }


}
