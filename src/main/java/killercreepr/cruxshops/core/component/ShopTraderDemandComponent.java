package killercreepr.cruxshops.core.component;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxshops.api.component.ShopTraderComponent;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeObject;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.shop.trade.CruxCurrencyTradeObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopTraderDemandComponent implements ShopTraderComponent {
    protected final @Nullable TradeModifier buyingModifier;
    protected final @Nullable TradeModifier sellingModifier;

    public ShopTraderDemandComponent(@Nullable TradeModifier buyingModifier, @Nullable TradeModifier sellingModifier) {
        this.buyingModifier = buyingModifier;
        this.sellingModifier = sellingModifier;
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
        protected final @Nullable NumberProvider priceClamp;

        public TradeModifier(double modifier, @Nullable NumberProvider priceClamp) {
            this.modifier = modifier;
            this.priceClamp = priceClamp;
        }

        public boolean canAdjust(ShopTradeObject object){
            return object instanceof CruxCurrencyTradeObject;
        }

        public double getModifier() {
            return modifier;
        }

        @Nullable
        public NumberProvider getPriceClamp() {
            return priceClamp;
        }
    }
}
