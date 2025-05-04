package killercreepr.cruxshops.api.shop.trade;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ShopTrade {
    @NotNull
    List<ShopTradeIngredient> getIngredients();
    @NotNull List<ShopTradeResult> getResults();

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
