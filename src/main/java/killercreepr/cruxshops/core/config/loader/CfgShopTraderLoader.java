package killercreepr.cruxshops.core.config.loader;

import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.config.handler.FileShopTrader;
import killercreepr.cruxshops.core.registries.ShopsRegistries;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class CfgShopTraderLoader extends CfgLoader {
    protected final FileShopTrader fileShopTrader;

    public CfgShopTraderLoader(FileShopTrader fileShopTrader) {
        this.fileShopTrader = fileShopTrader;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path) {
        ShopTrader table;
        if (path == null) {
            table = cfg.deserialize("", ShopTrader.class);
        } else {
            FileElement var5 = cfg.getRoot();
            if (!(var5 instanceof FileObject root)) {
                return;
            }

            table = fileShopTrader.deserializeFromFile(new FileContext<>(cfg.fileRegistry()), root, Crux.key(path));
        }

        if (table != null) {
            Crux.log(Level.INFO, "Registered shop trader: " + ((Keyed) table).key());
            ShopsRegistries.SHOP_TRADER.register(table);
        }
    }
}
