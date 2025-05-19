package killercreepr.cruxshops.core.component;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxshops.api.component.ShopTraderComponent;
import killercreepr.cruxshops.api.data.OriginalHolder;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeObject;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.shop.trade.CruxCurrencyTradeObject;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopTraderDemandComponent implements ShopTraderComponent {
    protected final @Nullable NumberProvider demandTick;
    protected final @Nullable NumberProvider demandAdd;
    protected final @Nullable NumberProvider supplyAdd;
    protected final @Nullable NumberProvider demandMultiplier;
    protected final @Nullable NumberProvider supplyMultiplier;
    protected final @Nullable TradeModifier buyingModifier;
    protected final @Nullable TradeModifier sellingModifier;

    public ShopTraderDemandComponent(@Nullable NumberProvider demandTick, @Nullable NumberProvider demandAdd, @Nullable NumberProvider supplyAdd, @Nullable NumberProvider demandMultiplier, @Nullable NumberProvider supplyMultiplier, @Nullable TradeModifier buyingModifier, @Nullable TradeModifier sellingModifier) {
        this.demandTick = demandTick;
        this.demandAdd = demandAdd;
        this.supplyAdd = supplyAdd;
        this.demandMultiplier = demandMultiplier;
        this.supplyMultiplier = supplyMultiplier;
        this.buyingModifier = buyingModifier;
        this.sellingModifier = sellingModifier;
    }


    @Nullable
    public NumberProvider getDemandTick() {
        return demandTick;
    }

    @Nullable
    public NumberProvider getDemandAdd() {
        return demandAdd;
    }

    @Nullable
    public NumberProvider getSupplyAdd() {
        return supplyAdd;
    }

    @Nullable
    public NumberProvider getDemandMultiplier() {
        return demandMultiplier;
    }

    @Nullable
    public NumberProvider getSupplyMultiplier() {
        return supplyMultiplier;
    }

    @Override
    public void load(@NotNull FileContext<?> ctx, @NotNull FileObject object, @NotNull ShopTrader trader){

        if((object.get("trader_demand") instanceof FileArray a)){
            a.forEach(ele ->{
                var loaded = TraderTradeDemandComponent.load(ctx, trader, ele);
                if(loaded == null) return;
                trader.getProfession().getAllTrades().forEach(traderTrade ->{
                    var data = traderTrade.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
                    if(data==null) return;
                    if(!data.key().equals(loaded.key())) return;
                    data.setDemand(loaded.getDemand());
                    data.setSupply(loaded.getSupply());

                    if(loaded instanceof CacheTraderTradeDemandComponent loadC && data instanceof CacheTraderTradeDemandComponent c){
                        c.setCacheDemand(loadC.getCacheDemand());
                        c.setCacheSupply(loadC.getCacheSupply());
                    }
                });
            });
        }
    }

    @Override
    public void save(@NotNull FileContext<?> ctx, @NotNull FileObject object, @NotNull ShopTrader trader){
        FileArray a = new FileArray(trader.getProfession().getAllTrades().size());
        trader.getProfession().getAllTrades().forEach(trade ->{
            var data = trade.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
            if(data!=null){
                var saved = data.save(ctx, trader, trade);
                if(saved!=null) a.add(saved);
            }
        });
        object.add("trader_demand", a);
    }

    @Override
    public void onTradePurchased(@NotNull ShopTrader trader, @NotNull Entity e, @NotNull TraderTrade traderTrade, @NotNull ShopTrade trade) {
        var original = OriginalHolder.getCompleteOriginalOrThis(traderTrade);
        var data = original.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
        if(data == null) return;
        if(trade.equals(traderTrade.getBuyingTrade())){
            if(data instanceof CacheTraderTradeDemandComponent c) c.addCacheDemand(1);
            else data.addDemand(1);
        }else if(trade.equals(traderTrade.getSellingTrade())){
            if(data instanceof CacheTraderTradeDemandComponent c) c.addCacheSupply(1);
            else data.addSupply(1);
        }else throw new UnsupportedOperationException("What the heck??");
    }

    @Nullable
    public TradeModifier getBuyingModifier() {
        return buyingModifier;
    }

    @Nullable
    public TradeModifier getSellingModifier() {
        return sellingModifier;
    }

    @Nullable
    @Override
    public TraderTrade adjustTrade(@NotNull ShopTrader trader, @NotNull Entity viewer, @NotNull TraderTrade trade) {
        TraderTradeDemandComponent data = trade.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
        if(data==null) return trade;
        ShopTrade buying = trade.getBuyingTrade();
        ShopTrade selling = trade.getSellingTrade();
        if(buying !=null){
            buying = data.adjustTrade(trade, buying, buyingModifier);
        }
        if(selling != null){
            selling = data.adjustTrade(trade, selling, sellingModifier);
        }
        if(buying == null && selling == null) return null;
        return trade.withTrades(buying, selling);
    }

    protected int nextDemand = -1;
    @Override
    public void tick(int tick, int delay, @NotNull ShopTrader trader) {
        if(demandTick == null) return;

        if(nextDemand == -1){
            nextDemand = tick + demandTick.value().intValue();
            return;
        }
        if(tick < nextDemand){
            return;
        }
        nextDemand = tick + demandTick.value().intValue();

        trader.getProfession().getAllTrades().forEach(trade ->{
            var data = trade.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
            if(data==null) return;
            if(demandMultiplier != null){
                if(data instanceof CacheTraderTradeDemandComponent c){
                    c.setCacheDemand(
                        (int) (c.getCacheDemand() * demandMultiplier.value().floatValue())
                    );
                }else{
                    data.setDemand(
                        (int) (data.getDemand() * demandMultiplier.value().floatValue())
                    );
                }
            }
            if(supplyMultiplier != null){
                if(data instanceof CacheTraderTradeDemandComponent c){
                    c.setCacheSupply(
                        (int) (c.getCacheSupply() * supplyMultiplier.value().floatValue())
                    );
                }else{
                    data.setSupply(
                        (int) (data.getSupply() * supplyMultiplier.value().floatValue())
                    );
                }
            }
            if(demandAdd != null){
                if(data instanceof CacheTraderTradeDemandComponent c){
                    c.addCacheDemand(demandAdd.value().intValue());
                } else data.addDemand(demandAdd.value().intValue());
            }
            if(supplyAdd != null){
                if(data instanceof CacheTraderTradeDemandComponent c){
                    c.addCacheSupply(supplyAdd.value().intValue());
                }else data.addSupply(supplyAdd.value().intValue());
            }
        });
    }

    public static class TradeModifier{
        protected final double buyModifier;
        protected final double sellModifier;
        protected final @Nullable Integer priceClampMin;
        protected final @Nullable Integer priceClampMax;

        public TradeModifier(double buyModifier, double sellModifier, @Nullable Integer priceClampMin, @Nullable Integer priceClampMax) {
            this.buyModifier = buyModifier;
            this.sellModifier = sellModifier;
            this.priceClampMin = priceClampMin;
            this.priceClampMax = priceClampMax;
        }

        public boolean canAdjust(ShopTradeObject object){
            return object instanceof CruxCurrencyTradeObject;
        }

        public double getBuyModifier() {
            return buyModifier;
        }

        public double getSellModifier() {
            return sellModifier;
        }

        public int clampPrice(int price, int originalPrice){
            if(priceClampMin != null && price < (originalPrice+priceClampMin)){
                return originalPrice+priceClampMin;
            }
            if(priceClampMax != null && price > (originalPrice+priceClampMax)){
                return originalPrice+priceClampMax;
            }
            return price;
        }

        @Nullable
        public Integer getPriceClampMin() {
            return priceClampMin;
        }

        @Nullable
        public Integer getPriceClampMax() {
            return priceClampMax;
        }
    }
}
