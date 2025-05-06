package killercreepr.cruxshops.core.trader;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.data.PluginLoadable;
import killercreepr.crux.api.data.tick.TickedTime;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.cruxconfig.config.bukkit.file.BukkitDataFile;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxshops.api.component.ShopTraderComponent;
import killercreepr.cruxshops.api.event.EntityPurchaseTraderTradeEvent;
import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleShopTrader extends DataComponentHandler.Simple implements ShopTrader, Keyed, TickedTime, PluginLoadable {
    protected final Key key;
    protected final TraderProfession profession;
    protected List<TraderTrade> trades = new ArrayList<>();

    public SimpleShopTrader(Key key, TraderProfession profession, Collection<TypedDataComponent<?>> components) {
        this.key = key;
        this.profession = profession;
        setTrades(profession.getAllTrades());
        if(components == null) return;
        components.forEach(this::set);
    }

    @NotNull
    @Override
    public TraderProfession getProfession() {
        return profession;
    }

    @Override
    public void setTrades(@NotNull List<TraderTrade> trades) {
        this.trades.clear();
        this.trades.addAll(trades);
    }

    @Nullable
    @Override
    public TraderTrade adjustTrade(@NotNull Entity viewer, @NotNull TraderTrade trade) {
        var data = getAllOfType(ShopTraderComponent.class);
        if(!data.isEmpty()){
            TraderTrade adjusted = trade;
            for(var comp : data){
                adjusted = comp.adjustTrade(this, viewer, adjusted);
                if(adjusted == null) break;
            }
            return adjusted;
        }
        return trade;
    }

    @NotNull
    @Override
    public List<TraderTrade> adjustTrades(@NotNull Entity viewer, @NotNull List<TraderTrade> trades) {
        if(trades.isEmpty()) return trades;
        var data = getAllOfType(ShopTraderComponent.class);
        if(data.isEmpty()) return trades;
        int index = -1;
        for(TraderTrade trade : new ArrayList<>(trades)){
            index++;
            TraderTrade adjusted = trade;
            for(var comp : data){
                adjusted = comp.adjustTrade(this, viewer, adjusted);
                if(adjusted == null){
                    trades.remove(index);
                    index--;
                    break;
                }
                if(adjusted==trade) continue;
                trades.set(index, adjusted);
            }
        }
        return trades;
    }

    @Override
    public MergedTagContainer buildTags(ShopTrade trade){
        var tags = TagContainer.merged();
        forEachAllOfType(ShopTraderComponent.class, data -> tags.addAll(data.buildTags(this, trade)));
        return tags;
    }

    @Override
    public boolean purchaseTrade(@NotNull Entity e, @NotNull TraderTrade traderTrade, @NotNull ShopTrade trade) {
        EntityPurchaseTraderTradeEvent event = new EntityPurchaseTraderTradeEvent(e, this, trade);
        if(!event.callEvent()) return false;
        trade = event.getTrade();

        ShopTrade finalTrade = trade;
        forEachAllOfType(ShopTraderComponent.class, data ->{
            data.onTradePurchased(this, e, traderTrade, finalTrade);
        });

        trade.purchase(e);
        return true;
    }

    @Override
    public @NotNull CanPurchase canPurchaseTrade(@NotNull Entity p, @NotNull ShopTrade trade) {
        if(!trade.canAfford(p)) return CanPurchase.CANNOT_AFFORD;
        if(!trade.canAccept(p)) return CanPurchase.CANNOT_ACCEPT;

        //UUID uuid = p.getUniqueId();
        /*if(trade instanceof BuyerLimitedTrade limited){
            if(limited.getUses(uuid) < 1) return CanPurchase.NOT_ENOUGH_STOCK;
            return CanPurchase.TRUE;
        }*/

        /*StockManager stockManager = shop.getStockManager();
        if(stockManager.hasUnlimitedStock()) return CanPurchase.TRUE;

        for(CruxTradeResult result : trade.getResults()){
            if(stockManager.getStock(result) < result.getAmount(uuid)) return CanPurchase.NOT_ENOUGH_STOCK;
        }*/
        return CanPurchase.TRUE;
    }

    @NotNull
    @Override
    public List<TraderTrade> getTrades(@NotNull Entity viewer) {
        List<TraderTrade> trades = new ArrayList<>(this.trades);
        trades.removeIf(t -> !t.canView(viewer));
        adjustTrades(viewer, trades);
        return trades;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public void tick(int tick, int delay) {
        forEach(typed ->{
            if(!(typed.getValue() instanceof ShopTraderComponent ticked)) return;
            ticked.tick(tick, delay, this);
        });
    }

    public DataFile getDataFile(Plugin plugin, boolean createIfNeeded){
        return BukkitDataFile.parseFromGeneralPath(
            new CruxFolder(plugin, "data/shop/" + CruxKey.toFileName(key) + ".json").file(),
            createIfNeeded
        );
    }

    @Override
    public void save(@NotNull Plugin plugin) {
        DataFile file = getDataFile(plugin, true);
        if(file == null) return;
        FileContext<?> ctx = new FileContext<>(file.fileRegistry());

        FileObject save = new FileObject();
        forEach(typed ->{
            if(!(typed.getValue() instanceof ShopTraderComponent data)) return;
            data.save(ctx, save, this);
        });
        if(save.isEmpty()){
            file.delete();
        }else{
            file.serialize("data", save);
            file.save();
        }
    }

    @Override
    public void load(@NotNull Plugin plugin) {
        DataFile file = getDataFile(plugin, false);
        if(file == null) return;
        if(!(file.getElement("data") instanceof FileObject o)){
            file.close();
            return;
        }
        file.close();
        FileContext<?> ctx = new FileContext<>(file.fileRegistry());
        forEach(typed ->{
            if(!(typed.getValue() instanceof ShopTraderComponent data)) return;
            data.load(ctx, o, this);
        });
    }
}
