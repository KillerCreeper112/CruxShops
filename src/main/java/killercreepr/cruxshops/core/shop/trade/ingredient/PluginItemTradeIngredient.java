package killercreepr.cruxshops.core.shop.trade.ingredient;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.core.shop.trade.PluginItemTradeObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

public class PluginItemTradeIngredient extends PluginItemTradeObject implements ShopTradeIngredient {
    public PluginItemTradeIngredient(ItemHolder itemHolder, int amount) {
        super(itemHolder, amount);
    }

    @Override
    public boolean canAfford(@NotNull Entity e) {
        if(!(e instanceof HumanEntity p)) return false;
        int x = getItemAmount(p);
        return x >= amount;
    }

    @Override
    public void accept(@NotNull Entity e) {
        if(!(e instanceof HumanEntity p)) return;
        removeItems(p);
    }

    public boolean isValidItem(ItemStack item){
        if(CruxItem.isEmpty(item)) return false;

        if(item.getItemMeta() instanceof Damageable meta && meta.getDamage() > 0) return false;

        return Crux.handlers().item().compare(itemHolder.key(), item);
    }

    public int getItemAmount(@NotNull HumanEntity p){
        int amount = 0;
        for(ItemStack item : p.getInventory()){
            if(isValidItem(item)) amount += item.getAmount();
        }
        return amount;
    }

    /**
     * @return The remaining amount of items that it could not remove.
     */
    public int removeItems(@NotNull HumanEntity p){
        int remaining = amount;
        for(ItemStack item : p.getInventory()){
            if(!isValidItem(item)) continue;
            int remove = Math.min(item.getAmount(), remaining);
            item.setAmount(item.getAmount()-remove);
            remaining -= remove;
            if(remaining < 1) break;
        }
        return remaining;
    }
}
