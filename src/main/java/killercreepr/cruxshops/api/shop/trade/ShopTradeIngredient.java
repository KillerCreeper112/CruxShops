package killercreepr.cruxshops.api.shop.trade;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ShopTradeIngredient extends ShopTradeObject {
    boolean canAfford(@NotNull Entity p);
    void accept(@NotNull Entity p);
    @NotNull
    ItemStack buildIcon();
}
