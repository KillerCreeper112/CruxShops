package killercreepr.cruxshops.core.shop.trade.result;

import killercreepr.crux.core.util.CruxEntityUtil;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.core.shop.trade.PluginItemTradeObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PluginItemTradeResult extends PluginItemTradeObject implements ShopTradeResult {
    public PluginItemTradeResult(ItemHolder itemHolder, int amount) {
        super(itemHolder, amount);
    }

    @Override
    public void give(@NotNull Entity e) {
        if(!(e instanceof HumanEntity p)) return;
        ItemStack i = item();
        CruxEntityUtil.giveOrDrop(p, drop ->{
            drop.setOwner(e.getUniqueId());
        }, i);
    }

    @Override
    public boolean canAccept(@NotNull Entity e) {
        if(!(e instanceof HumanEntity p)) return false;
        return p.getInventory().firstEmpty() != -1;
    }
}
