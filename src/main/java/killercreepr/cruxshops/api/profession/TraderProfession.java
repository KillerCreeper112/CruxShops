package killercreepr.cruxshops.api.profession;

import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TraderProfession {
    @NotNull List<TraderTrade> getAllTrades();
}
