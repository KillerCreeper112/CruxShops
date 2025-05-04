package killercreepr.cruxshops.core.shop;

import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.api.shop.trade.TradeViewCondition;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleShopTrade implements ShopTrade {
    protected final @NotNull List<ShopTradeIngredient> ingredients;
    protected final @NotNull List<ShopTradeResult> results;
    protected final @Nullable TradeViewCondition viewCondition;

    public SimpleShopTrade(@NotNull List<ShopTradeIngredient> ingredients, @NotNull List<ShopTradeResult> results, @Nullable TradeViewCondition viewCondition) {
        this.ingredients = ingredients;
        this.results = results;
        this.viewCondition = viewCondition;
    }

    @NotNull
    @Override
    public List<ShopTradeIngredient> getIngredients() {
        return ingredients;
    }

    @NotNull
    @Override
    public List<ShopTradeResult> getResults() {
        return results;
    }

    /**
     * @param p
     * @return Whether the player can afford all the ingredients.
     */
    @Override
    public boolean canAfford(@NotNull Entity p) {
        for(var i : getIngredients()){
            if(!i.canAfford(p)) return false;
        }
        return true;
    }

    /**
     * @param p
     * @return Whether the player is able to accept the results.
     * Primarily used to check if the player has enough inventory space for
     * item results.
     */
    @Override
    public boolean canAccept(@NotNull Entity p) {
        for(var i : getResults()){
            if(!i.canAccept(p)) return false;
        }
        return true;
    }

    /**
     * @param p
     * @return Whether the player is able to view this trade.
     */
    @Override
    public boolean canView(@NotNull Entity p) {
        return viewCondition == null || viewCondition.canView(p);
    }

    /**
     * Purchases this trade for the player.
     *
     * @param p
     */
    @Override
    public void purchase(@NotNull Entity p) {
        for(var i : getIngredients()){
            i.accept(p);
        }
        for(var result : getResults()){
            result.give(p);
        }
    }
}
