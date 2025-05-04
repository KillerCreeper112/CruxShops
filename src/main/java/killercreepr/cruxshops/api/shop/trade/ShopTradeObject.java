package killercreepr.cruxshops.api.shop.trade;

import org.jetbrains.annotations.NotNull;

public interface ShopTradeObject {
    int getAmount();
    @NotNull
    String getDisplayName();
}
