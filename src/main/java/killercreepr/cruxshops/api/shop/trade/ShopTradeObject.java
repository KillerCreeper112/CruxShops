package killercreepr.cruxshops.api.shop.trade;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopTradeObject {
    int getAmount();
    @NotNull
    String getDisplayName();

    @Contract(pure = true)
    ShopTradeObject withAmount(int amount);

    @Nullable ShopTradeObject getOriginal();
}
