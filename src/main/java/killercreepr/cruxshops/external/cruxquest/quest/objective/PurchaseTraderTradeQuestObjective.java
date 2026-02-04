package killercreepr.cruxshops.external.cruxquest.quest.objective;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxquest.core.quest.objective.QuestObjectiveCommonData;
import killercreepr.cruxquest.core.quest.objective.standard.GenericEventQuestObjective;
import killercreepr.cruxshops.api.event.EntityPurchaseTraderTradeEvent;
import killercreepr.cruxshops.core.loot.CruxShopEventLootContexts;
import org.jetbrains.annotations.NotNull;

public class PurchaseTraderTradeQuestObjective extends GenericEventQuestObjective<EntityPurchaseTraderTradeEvent> {
    public PurchaseTraderTradeQuestObjective(@NotNull QuestObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(EntityPurchaseTraderTradeEvent event) {
        return CruxShopEventLootContexts.builder(event).build();
    }
}
