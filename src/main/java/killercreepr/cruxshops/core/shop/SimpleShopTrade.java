package killercreepr.cruxshops.core.shop;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleShopTrade implements ShopTrade {
    protected final @NotNull List<ShopTradeIngredient> ingredients;
    protected final @NotNull List<ShopTradeResult> results;
    protected final @Nullable LootCondition viewCondition;
    protected final @Nullable ShopTrade originalTrade;

    public SimpleShopTrade(@NotNull List<ShopTradeIngredient> ingredients, @NotNull List<ShopTradeResult> results, @Nullable LootCondition viewCondition, @Nullable ShopTrade originalTrade) {
        this.ingredients = ingredients;
        this.results = results;
        this.viewCondition = viewCondition;
        this.originalTrade = originalTrade;
    }

    /**
     * @return If the original trade was modified,
     * this will return the trade before it was modified.
     */
    @Nullable
    @Override
    public ShopTrade getOriginalTrade() {
        return originalTrade;
    }

    @Override
    public ShopTrade withResults(@NotNull List<ShopTradeResult> results) {
        return new SimpleShopTrade(
            new ArrayList<>(ingredients), results, viewCondition, this
        );
    }

    @Override
    public ShopTrade withIngredients(@NotNull List<ShopTradeIngredient> ingredients) {
        return new SimpleShopTrade(
            ingredients, new ArrayList<>(results), viewCondition, this
        );
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
        if(viewCondition == null) return true;
        LootContext ctx = LootContext.builder().looter(p).looted(this).build();
        return viewCondition.test(ctx);
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
