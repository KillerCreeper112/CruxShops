package killercreepr.cruxshops.core.profession;

import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleTraderProfession implements TraderProfession {
    protected final List<TraderTrade> trades;

    public SimpleTraderProfession(List<TraderTrade> trades) {
        this.trades = trades;
    }

    @NotNull
    @Override
    public List<TraderTrade> getAllTrades() {
        return trades;
    }
}
