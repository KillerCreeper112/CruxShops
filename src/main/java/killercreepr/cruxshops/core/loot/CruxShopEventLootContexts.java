package killercreepr.cruxshops.core.loot;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxshops.api.event.EntityPurchaseTraderTradeEvent;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CruxShopEventLootContexts {
    public static LootContext.Builder builder(@NotNull EntityPurchaseTraderTradeEvent event){
        Entity e = event.getEntity();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getTrader(), "merchant", "trader")
                    .putAll(event.getTrade(), "trade")
                    .build()
            )
            .location(e.getLocation())
            .looter(e)
            .looted(event.getTrader())
            ;
    }

    private static LootContext.Builder builder(){
        return LootContext.builder();
    }
}
