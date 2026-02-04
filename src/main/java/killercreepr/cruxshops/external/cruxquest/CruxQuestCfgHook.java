package killercreepr.cruxshops.external.cruxquest;

import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxquest.api.quest.component.QuestComponent;
import killercreepr.cruxquest.api.quest.component.QuestObjectiveComponent;
import killercreepr.cruxquest.api.quest.objective.QuestObjective;
import killercreepr.cruxquest.core.CruxQuestPlugin;
import killercreepr.cruxquest.core.config.handler.CustomFileQuestObjective;
import killercreepr.cruxquest.core.config.handler.FileQuestComponent;
import killercreepr.cruxquest.core.config.handler.FileQuestObjective;
import killercreepr.cruxquest.core.config.handler.FileQuestObjectiveComponent;
import killercreepr.cruxquest.core.quest.objective.QuestObjectiveCommonData;
import killercreepr.cruxshops.core.CruxShopsPlugin;
import killercreepr.cruxshops.external.cruxquest.quest.objective.PurchaseTraderTradeQuestObjective;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CruxQuestCfgHook {
  public final FileQuestObjective QUEST_OBJECTIVE;
  public final FileQuestComponent QUEST_COMPONENT;
  public final FileQuestObjectiveComponent QUEST_OBJECTIVE_COMPONENT;

  public CruxQuestCfgHook(FileQuestObjective questObjective, FileQuestComponent QUEST_COMPONENT, FileQuestObjectiveComponent QUEST_OBJECTIVE_COMPONENT) {
    QUEST_OBJECTIVE = questObjective;
    this.QUEST_COMPONENT = QUEST_COMPONENT;
    this.QUEST_OBJECTIVE_COMPONENT = QUEST_OBJECTIVE_COMPONENT;
  }

  public void register() {
    registerStandardObjectives();
    registerLootConditions();
    registerComponents();
    registerLootFunctions();
  }

  public void registerQuestAndObjComp(Key key, FileObjectHandler<?> handler) {
    QUEST_COMPONENT.register(key, (FileObjectHandler<? extends QuestComponent>) handler);
    QUEST_OBJECTIVE_COMPONENT.register(key, (FileObjectHandler<? extends QuestObjectiveComponent>) handler);
  }

  public void registerQuestComp(Key key, FileObjectHandler<?> handler) {
    QUEST_COMPONENT.register(key, (FileObjectHandler<? extends QuestComponent>) handler);
  }

  public void registerQuestObjComp(Key key, FileObjectHandler<?> handler) {
    QUEST_OBJECTIVE_COMPONENT.register(key, (FileObjectHandler<? extends QuestObjectiveComponent>) handler);
  }

  public void registerStandardObjectives() {
    QUEST_OBJECTIVE.registerCustomHandler(new CustomFileQuestObjective.Simple<>(CruxShopsPlugin.inst().key("purchase_trader_trade")) {
      @Override
      public QuestObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull QuestObjectiveCommonData data) {
        Integer maxProgress = e.getObject(Integer.class, "amount");
        if (maxProgress == null) maxProgress = 1;
        return new PurchaseTraderTradeQuestObjective(data, maxProgress);
      }
    });
  }

  public static void registerLootConditions() {
  }

  public void registerComponents() {
    var handlers = BukkitCfgHandlers.TYPED_DATA_COMPONENT.typeHandlers();

  }

  public static void registerLootFunctions() {
    var func = BukkitCfgHandlers.ITEM_LOOT_FUNCTION;
    var plugin = CruxQuestPlugin.inst();
  }
}
