package killercreepr.cruxshops.core.registries;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.IndirectKeyedRegistry;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Key;

public class ShopsRegistries {
    public static final MappedRegistry<Key, ShopTrader> SHOP_TRADER = new IndirectKeyedRegistry<>();
}
