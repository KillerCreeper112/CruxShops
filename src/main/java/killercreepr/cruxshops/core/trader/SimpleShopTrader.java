package killercreepr.cruxshops.core.trader;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.data.tick.TickedTime;
import killercreepr.cruxshops.api.component.ShopTraderComponent;
import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleShopTrader extends DataComponentHandler.Simple implements ShopTrader, Keyed, TickedTime {
    protected final Key key;
    protected final TraderProfession profession;
    protected List<TraderTrade> trades = new ArrayList<>();

    public SimpleShopTrader(Key key, TraderProfession profession, Collection<TypedDataComponent<?>> components) {
        this.key = key;
        this.profession = profession;
        if(components == null) return;
        components.forEach(this::set);
    }

    @NotNull
    @Override
    public TraderProfession getProfession() {
        return profession;
    }

    @Override
    public void setTrades(@NotNull List<TraderTrade> trades) {
        this.trades.clear();
        this.trades.addAll(trades);
    }

    @NotNull
    @Override
    public List<TraderTrade> getTrades(@NotNull Entity viewer) {
        if(trades.isEmpty()) setTrades(profession.getAllTrades());

        if(!trades.isEmpty()){
            var data = getAllOfType(ShopTraderComponent.class);
            if(!data.isEmpty()){
                int index = -1;
                for(TraderTrade trade : new ArrayList<>(trades)){
                    index++;
                    TraderTrade adjusted = trade;
                    for(var comp : data){
                        adjusted = comp.adjustTrade(this, adjusted);
                        if(adjusted == null){
                            trades.remove(index);
                            index--;
                            break;
                        }
                        if(adjusted==trade) continue;
                        trades.set(index, adjusted);
                    }
                }
            }
        }

        return trades;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public void tick(int tick, int delay) {

    }
}
