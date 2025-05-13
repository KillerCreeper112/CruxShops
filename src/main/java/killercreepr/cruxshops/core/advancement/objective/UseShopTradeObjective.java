package killercreepr.cruxshops.core.advancement.objective;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxshops.api.event.EntityPurchaseTraderTradeEvent;
import killercreepr.cruxshops.core.loot.CruxShopEventLootContexts;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UseShopTradeObjective extends NumberObjective {
    public UseShopTradeObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull EntityPurchaseTraderTradeEvent event){
        LootContext ctx = CruxShopEventLootContexts.builder(event).build();
        return trigger(
            who, manager, advancement, ctx
        );
    }
}
