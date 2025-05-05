package killercreepr.cruxshops.core.trader;

import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleShopTrader implements ShopTrader, Keyed {
    protected final Key key;
    protected final TraderProfession profession;
    protected List<TraderTrade> trades = new ArrayList<>();

    public SimpleShopTrader(Key key, TraderProfession profession) {
        this.key = key;
        this.profession = profession;
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
        return trades;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
