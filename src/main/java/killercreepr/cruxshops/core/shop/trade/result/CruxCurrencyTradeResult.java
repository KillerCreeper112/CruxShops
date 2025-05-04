package killercreepr.cruxshops.core.shop.trade.result;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxcurrency.api.currency.CruxCurrency;
import killercreepr.cruxcurrency.core.CruxCurrencyPlugin;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.core.shop.trade.CruxCurrencyTradeObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CruxCurrencyTradeResult extends CruxCurrencyTradeObject implements ShopTradeResult {
    public CruxCurrencyTradeResult(Holder<CruxCurrency> currencyHolder, int amount) {
        super(currencyHolder, amount);
    }

    @Override
    public void give(@NotNull Entity p) {
        if(!(p instanceof OfflinePlayer player)) return;
        CruxCurrencyPlugin.inst().ecoHandler().depositBalance(player, currencyHolder.value(), amount);
    }

    @Override
    public boolean canAccept(@NotNull Entity p) {
        return true;
    }
}
