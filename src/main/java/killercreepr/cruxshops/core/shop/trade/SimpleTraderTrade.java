package killercreepr.cruxshops.core.shop.trade;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.cruxshops.api.component.TraderTradeComponent;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class SimpleTraderTrade extends DataComponentHandler.Simple implements TraderTrade {
    protected final ShopTrade buyingTrade;
    protected final ShopTrade sellingTrade;

    public SimpleTraderTrade(ShopTrade buyingTrade, ShopTrade sellingTrade, Collection<TypedDataComponent<?>> components) {
        this.buyingTrade = buyingTrade;
        this.sellingTrade = sellingTrade;
        if(components == null) return;
        components.forEach(this::set);
    }

    @Nullable
    @Override
    public ShopTrade getBuyingTrade() {
        return buyingTrade;
    }

    @Nullable
    @Override
    public ShopTrade getSellingTrade() {
        return sellingTrade;
    }

    @Override
    public boolean canView(Entity e) {
        if((buyingTrade != null && !buyingTrade.canView(e)) || (sellingTrade != null && !sellingTrade.canView(e))) return false;
        return true;
    }

    public Collection<TypedDataComponent<?>> buildCopiedTypedCollection() {
        Collection<TypedDataComponent<?>> list = new HashSet<>();
        this.map.forEach((type, value) ->{
            if(value.value() instanceof TraderTradeComponent c){
                list.add(TypedDataComponent.createUnchecked(type, c.createCopy()));
            }else list.add(TypedDataComponent.createUnchecked(type, value.value()));
        });
        return list;
    }

    @Override
    public TraderTrade withBuyingTrade(@Nullable ShopTrade buyingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade, buildCopiedTypedCollection());
    }

    @Override
    public TraderTrade withSellingTrade(@Nullable ShopTrade sellingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade, buildCopiedTypedCollection());
    }

    @Override
    public TraderTrade withTrades(@Nullable ShopTrade buyingTrade, @Nullable ShopTrade sellingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade, buildCopiedTypedCollection());
    }
}
