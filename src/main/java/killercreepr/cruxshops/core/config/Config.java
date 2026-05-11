package killercreepr.cruxshops.core.config;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxshops.core.config.object.DatabaseDetails;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Config extends Cfg {
    public final CfgValue<Boolean> USURVIVE_SPAWN_LOGIC = new CommonValue<>(true){};
    public final CfgValue<DynamicItem> SELLING_ITEM = new CommonValue<>(){};
    public final CfgValue<DynamicItem> BUYING_ITEM = new CommonValue<>(){};

    public final CfgValue<DynamicItem> RESULT_BOTH_ITEM = new CommonValue<>(){};
    public final CfgValue<DynamicItem> RESULT_BUYING_ITEM = new CommonValue<>(){};
    public final CfgValue<DynamicItem> RESULT_SELLING_ITEM = new CommonValue<>(){};

    public final CfgValue<DatabaseDetails> SHOP_DATABASE_DETAILS = new  CommonValue<>(){};

    public Config(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public Config(@NotNull File file) {
        super(file);
    }

    public Config(@NotNull CruxConfig cfg) {
        super(cfg);
    }
}
