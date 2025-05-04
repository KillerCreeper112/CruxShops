package killercreepr.cruxshops.api.trader;

import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ShopTrader extends TraderTradesHolder {
    @NotNull TraderProfession getProfession();
    void setTrades(@NotNull List<TraderTrade> trades);
}
