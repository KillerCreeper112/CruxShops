package killercreepr.cruxshops.api.shop.trade;

import killercreepr.cruxshops.api.data.OriginalHolder;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ShopTrade extends OriginalHolder {
    @NotNull
    List<ShopTradeIngredient> getIngredients();
    @NotNull List<ShopTradeResult> getResults();

    @Contract(pure = true)
    ShopTrade withIngredients(@NotNull List<ShopTradeIngredient> ingredients);
    @Contract(pure = true)
    ShopTrade withResults(@NotNull List<ShopTradeResult> results);

    /**
     * @return If the original trade was modified,
     * this will return the trade before it was modified.
     */
    @Nullable ShopTrade getOriginal();

    /**
     * @return Whether the player can afford all the ingredients.
     */
    boolean canAfford(@NotNull Entity p);

    /**
     * @return Whether the player is able to accept the results.
     * Primarily used to check if the player has enough inventory space for
     * item results.
     */
    boolean canAccept(@NotNull Entity p);

    /**
     *
     * @return Whether the player is able to view this trade.
     */
    boolean canView(@NotNull Entity p);

    /**
     * Purchases this trade for the player.
     */
    void purchase(@NotNull Entity p);
}
