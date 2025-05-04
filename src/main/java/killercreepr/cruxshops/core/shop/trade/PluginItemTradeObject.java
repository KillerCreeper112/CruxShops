package killercreepr.cruxshops.core.shop.trade;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxshops.api.shop.trade.ShopTradeObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PluginItemTradeObject implements ShopTradeObject {
    protected final ItemHolder itemHolder;
    protected final int amount;

    public PluginItemTradeObject(ItemHolder itemHolder, int amount) {
        this.itemHolder = itemHolder;
        this.amount = amount;
    }

    public ItemStack item(){
        return itemHolder.value();
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "";
    }

    @NotNull
    public ItemStack buildIcon() {
        var item = item();
        item.setAmount(CruxMath.clamp(
            amount, 1, CruxItem.getMaxStackSize(item)
        ));
        return item;
    }
}
