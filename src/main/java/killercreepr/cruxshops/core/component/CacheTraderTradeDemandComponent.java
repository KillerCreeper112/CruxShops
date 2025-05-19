package killercreepr.cruxshops.core.component;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CacheTraderTradeDemandComponent extends TraderTradeDemandComponent{
    protected int cacheDemand;
    protected int cacheSupply;
    public CacheTraderTradeDemandComponent(Key key) {
        super(key);
    }

    @Nullable
    @Override
    public FileElement save(@NotNull FileContext<?> ctx, @NotNull ShopTrader trader, @NotNull TraderTrade trade) {
        FileObject o = (FileObject) super.save(ctx, trader, trade);
        o.addProperty("cache_demand", cacheDemand);
        o.addProperty("cache_supply", cacheSupply);
        return o;
    }

    public int addCacheDemand(int amount){
        setCacheDemand(cacheDemand+amount);
        return cacheDemand;
    }
    public int addCacheSupply(int amount){
        setCacheSupply(cacheSupply+amount);
        return cacheSupply;
    }

    public int subtractCacheDemand(int amount){
        setCacheDemand(cacheDemand-amount);
        return cacheDemand;
    }
    public int subtractCacheSupply(int amount){
        setCacheSupply(cacheSupply-amount);
        return cacheSupply;
    }

    public int getCacheDemand() {
        return cacheDemand;
    }

    public void setCacheDemand(int demand) {
        this.cacheDemand = Math.max(demand, 0);
    }

    public int getCacheSupply() {
        return cacheSupply;
    }

    public void setCacheSupply(int supply) {
        this.cacheSupply = Math.max(supply, 0);
    }
}
