package killercreepr.cruxshops.core.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import killercreepr.cruxshops.api.event.EntityPurchaseTraderTradeEvent;
import killercreepr.cruxshops.core.advancement.objective.UseShopTradeObjective;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class CustomObjectiveListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityPurchaseMerchantTrade(EntityPurchaseTraderTradeEvent event) {
        Entity p = event.getEntity();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(UseShopTradeObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    private AdvancementHolder holder(@NotNull Entity p){
        return EntityMemory.getDataHolder(p, AdvancementHolder.class);
    }
}
