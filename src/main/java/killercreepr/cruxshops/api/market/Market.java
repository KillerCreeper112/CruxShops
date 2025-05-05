package killercreepr.cruxshops.api.market;

import killercreepr.crux.api.data.PluginLoadable;
import killercreepr.crux.api.game.Statutable;
import killercreepr.crux.api.registry.Registry;
import killercreepr.cruxshops.api.trader.ShopTrader;
import org.jetbrains.annotations.NotNull;

public interface Market extends Statutable, PluginLoadable {
    @NotNull
    Registry<ShopTrader> getMerchants();
    /*@NotNull
    NumberProvider getRestockEvery();
    int getCurrentRestockTime();
    void restock();
    void reduceDemand();*/
}
