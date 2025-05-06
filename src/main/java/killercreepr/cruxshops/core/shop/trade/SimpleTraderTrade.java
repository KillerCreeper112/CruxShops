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
    protected final TraderTrade original;

    public SimpleTraderTrade(ShopTrade buyingTrade, ShopTrade sellingTrade, Collection<TypedDataComponent<?>> components, TraderTrade original) {
        this.buyingTrade = buyingTrade;
        this.sellingTrade = sellingTrade;
        this.original = original;
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
        if(buyingTrade != null){
            if(!buyingTrade.canView(e)) return false;
        }
        if(sellingTrade != null){
            if(!sellingTrade.canView(e)) return false;
        }
        return true;
    }

    @Nullable
    @Override
    public TraderTrade getOriginal() {
        return original;
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
        return new SimpleTraderTrade(buyingTrade, sellingTrade, buildCopiedTypedCollection(), this);
    }

    @Override
    public TraderTrade withSellingTrade(@Nullable ShopTrade sellingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade, buildCopiedTypedCollection(), this);
    }

    @Override
    public TraderTrade withTrades(@Nullable ShopTrade buyingTrade, @Nullable ShopTrade sellingTrade) {
        return new SimpleTraderTrade(buyingTrade, sellingTrade, buildCopiedTypedCollection(), this);
    }
}
