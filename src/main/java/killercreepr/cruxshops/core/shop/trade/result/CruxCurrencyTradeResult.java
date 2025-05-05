package killercreepr.cruxshops.core.shop.trade.result;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxcurrency.api.currency.CruxCurrency;
import killercreepr.cruxcurrency.core.CruxCurrencyPlugin;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.core.shop.trade.CruxCurrencyTradeObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxCurrencyTradeResult extends CruxCurrencyTradeObject implements ShopTradeResult {
    public CruxCurrencyTradeResult(Holder<CruxCurrency> currencyHolder, int amount, ShopTradeResult original) {
        super(currencyHolder, amount, original);
    }

    @Nullable
    @Override
    public ShopTradeResult getOriginal() {
        return (ShopTradeResult) super.getOriginal();
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

    @Override
    public ShopTradeResult withAmount(int amount) {
        return new CruxCurrencyTradeResult(currencyHolder, amount, this);
    }
}
