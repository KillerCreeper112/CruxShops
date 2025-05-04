package killercreepr.cruxshops.api.shop.trade;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ShopTradeResult extends ShopTradeObject {
    void give(@NotNull Entity p);
    boolean canAccept(@NotNull Entity p);
    @NotNull
    ItemStack buildIcon();
}
