package killercreepr.cruxshops.core.shop.trade.ingredient;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxcurrency.api.currency.CruxCurrency;
import killercreepr.cruxcurrency.core.CruxCurrencyPlugin;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.core.shop.trade.CruxCurrencyTradeObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CruxCurrencyTradeIngredient extends CruxCurrencyTradeObject implements ShopTradeIngredient {
    public CruxCurrencyTradeIngredient(Holder<CruxCurrency> currencyHolder, int amount) {
        super(currencyHolder, amount);
    }

    @Override
    public boolean canAfford(@NotNull Entity p) {
        if(!(p instanceof OfflinePlayer player)) return false;
        return CruxCurrencyPlugin.inst().ecoHandler().hasBalance(player, currencyHolder.value(), amount);
    }

    @Override
    public void accept(@NotNull Entity p) {
        if(!(p instanceof OfflinePlayer player)) return;
        CruxCurrencyPlugin.inst().ecoHandler().withdrawBalance(player, currencyHolder.value(), amount);
    }
}
