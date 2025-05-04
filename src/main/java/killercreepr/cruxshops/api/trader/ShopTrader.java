package killercreepr.cruxshops.api.trader;

import killercreepr.cruxshops.api.profession.TraderProfession;
import org.jetbrains.annotations.NotNull;

public interface ShopTrader extends TraderTradesHolder {
    @NotNull TraderProfession getProfession();
}
