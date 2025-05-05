package killercreepr.cruxshops.core.shop.trade;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxcurrency.api.currency.CruxCurrency;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.registries.CruxItemRegistries;
import killercreepr.cruxshops.api.shop.trade.ShopTradeObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CruxCurrencyTradeObject implements ShopTradeObject {
    protected final Holder<CruxCurrency> currencyHolder;
    protected final int amount;
    protected final ShopTradeObject original;

    public CruxCurrencyTradeObject(Holder<CruxCurrency> currencyHolder, int amount, ShopTradeObject original) {
        this.currencyHolder = currencyHolder;
        this.amount = amount;
        this.original = original;
    }

    @Nullable
    @Override
    public ShopTradeObject getOriginal() {
        return original;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return currencyHolder.value().getDisplayName();
    }

    @NotNull
    public ItemStack buildIcon() {
        var currencyKey = currencyHolder.valueOrThrow().key();
        PluginItem item = CruxItemRegistries.ITEMS.get(currencyKey);
        if(item == null) throw new RuntimeException(currencyKey + " does not have an item form!");
        ItemStack i = item.buildItem();
        i.editMeta(meta ->{
            meta.setMaxStackSize(99);
        });
        i.setAmount(CruxMath.clamp(amount, 1, CruxItem.getMaxStackSize(i)));
        return i;
    }
}
