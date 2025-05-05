package killercreepr.cruxshops.api.shop.trade;

import killercreepr.cruxshops.api.data.OriginalHolder;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopTradeResult extends ShopTradeObject, OriginalHolder {
    void give(@NotNull Entity p);
    boolean canAccept(@NotNull Entity p);
    @NotNull
    ItemStack buildIcon();

    @Contract(pure = true)
    ShopTradeResult withAmount(int amount);

    @Nullable ShopTradeResult getOriginal();
}
