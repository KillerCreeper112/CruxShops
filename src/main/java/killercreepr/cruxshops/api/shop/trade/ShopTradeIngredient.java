package killercreepr.cruxshops.api.shop.trade;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopTradeIngredient extends ShopTradeObject {
    boolean canAfford(@NotNull Entity p);
    void accept(@NotNull Entity p);
    @NotNull
    ItemStack buildIcon();

    @Contract(pure = true)
    ShopTradeIngredient withAmount(int amount);

    @Nullable ShopTradeIngredient getOriginal();
}
