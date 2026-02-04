package killercreepr.cruxshops.external.cruxquest;

import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxquest.core.config.CfgHook;
import killercreepr.cruxshops.external.cruxquest.listener.QuestListener;

public class CruxQuestHook {
  public static void onLoad(){
    new CruxQuestCfgHook(
      CfgHook.QUEST_OBJECTIVE, CfgHook.QUEST_COMPONENT, CfgHook.QUEST_OBJECTIVE_COMPONENT
    ).register();
  }

  public static void onEnable(CruxPlugin plugin){
    plugin.registerListeners(new QuestListener());
  }
}
