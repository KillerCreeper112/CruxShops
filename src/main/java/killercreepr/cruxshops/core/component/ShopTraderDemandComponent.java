package killercreepr.cruxshops.core.component;

import killercreepr.cruxshops.api.component.ShopTraderComponent;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeObject;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.shop.trade.CruxCurrencyTradeObject;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopTraderDemandComponent implements ShopTraderComponent {
    protected final @Nullable TradeModifier buyingModifier;
    protected final @Nullable TradeModifier sellingModifier;

    public ShopTraderDemandComponent(@Nullable TradeModifier buyingModifier, @Nullable TradeModifier sellingModifier) {
        this.buyingModifier = buyingModifier;
        this.sellingModifier = sellingModifier;
    }

    @Override
    public void onTradePurchased(@NotNull ShopTrader trader, @NotNull Entity e, @NotNull TraderTrade traderTrade, @NotNull ShopTrade trade) {
        var data = traderTrade.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
        if(data == null) return;
        if(trade.equals(traderTrade.getBuyingTrade())){
            data.addSupply(1);
        }else if(trade.equals(traderTrade.getSellingTrade())){
            data.addDemand(1);
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
    public TraderTrade adjustTrade(@NotNull ShopTrader trader, @NotNull TraderTrade trade) {
        TraderTradeDemandComponent data = trader.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
        if(data==null) return trade;
        ShopTrade buying = trade.getBuyingTrade();
        ShopTrade selling = trade.getSellingTrade();
        if(buying !=null){
            buying = data.adjustTrade(buying, buyingModifier);
        }
        if(selling != null){
            selling = data.adjustTrade(selling, sellingModifier);
        }
        if(buying == null && selling == null) return null;
        return trade.withTrades(buying, selling);
    }

    public static class TradeModifier{
        protected final double modifier;
        protected final @Nullable Integer priceClampMin;
        protected final @Nullable Integer priceClampMax;

        public TradeModifier(double modifier, @Nullable Integer priceClampMin, @Nullable Integer priceClampMax) {
            this.modifier = modifier;
            this.priceClampMin = priceClampMin;
            this.priceClampMax = priceClampMax;
        }

        public boolean canAdjust(ShopTradeObject object){
            return object instanceof CruxCurrencyTradeObject;
        }

        public double getModifier() {
            return modifier;
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
