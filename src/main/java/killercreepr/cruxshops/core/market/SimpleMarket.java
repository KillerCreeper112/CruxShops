package killercreepr.cruxshops.core.market;

import killercreepr.crux.api.data.Loadable;
import killercreepr.crux.api.data.tick.TickedTime;
import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.game.SimpleStatutable;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.core.registries.Menus;
import killercreepr.cruxshops.api.market.Market;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.menu.ShopTraderMenu;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleMarket extends SimpleStatutable implements Market, Loadable {
    protected final @NotNull Plugin plugin;
    protected final int TICK_DELAY;
    protected final @NotNull Registry<ShopTrader> merchants;
    protected final @NotNull NumberProvider restockEvery;
    protected final @NotNull Map<Key, NumberProvider> restockValues;
    protected final @NotNull NumberProvider defaultRestockValue;
    protected final @NotNull NumberProvider reduceDemandEvery;

    public SimpleMarket(@NotNull Plugin plugin, int tickDelay, @NotNull Registry<ShopTrader> merchants,
                        @NotNull NumberProvider restockEvery,
                        @NotNull Map<Key, NumberProvider> restockValues,
                        @NotNull NumberProvider defaultRestockValue,
                        @NotNull NumberProvider reduceDemandEvery) {
        this.plugin = plugin;
        this.TICK_DELAY = tickDelay;
        this.merchants = merchants;
        this.restockEvery = restockEvery;
        this.restockValues = restockValues;
        this.defaultRestockValue = defaultRestockValue;
        this.reduceDemandEvery = reduceDemandEvery;
    }

    protected int tick = 0;
    protected int currentRestockTime;
    protected int currentReduceDemandTime;

    @NotNull
    public NumberProvider getReduceDemandEvery() {
        return reduceDemandEvery;
    }

    @NotNull
    @Override
    public Registry<ShopTrader> getMerchants() {
        return merchants;
    }

    @NotNull
    public NumberProvider getRestockEvery() {
        return restockEvery;
    }

    public int getCurrentRestockTime() {
        return currentRestockTime;
    }

    public void restock(){
        currentRestockTime = tick + restockEvery.value().intValue();

        /*for(CruxMerchant merchant : merchants){
            merchant.getShop().getTrades().forEach(trade ->{
                restock(trade, merchant.getShop().getStockManager());
            });
        }*/
    }

    /*public void restock(@NotNull CruxTrade trade, @NotNull StockManager stock){
        if(stock.hasUnlimitedStock()) return;
        for(CruxTradeResult result : trade.getResults()){
            if(!stock.isRestockable(result)) continue;
            if(!(result instanceof Keyed keyed)) continue;
            NumberProvider restockValue = restockValues.getOrDefault(keyed.key(), defaultRestockValue);
            int value = restockValue.value().intValue();
            if(value == 0) continue;
            if(CruxMath.testChance(40)){
                stock.removeStock(result, value);
                continue;
            }
            stock.addStock(result, value);
        }
    }*/

    public boolean shouldRestock(){
        return tick >= currentRestockTime;
    }

    public boolean shouldReduceDemand(){
        return tick >= currentReduceDemandTime;
    }

    public void updateMenus(){
        for(Menu menu : Menus.OPENED){
            if(!(menu instanceof ShopTraderMenu m)) continue;
            //Only refresh if it's a merchant from this market.
            //if(merchants.get(m.getMerchant().id()) == null) continue;
            m.refresh();
        }
    }

    public void reduceDemand(){
        currentReduceDemandTime = tick + reduceDemandEvery.value().intValue();
        for(ShopTrader merchant : merchants){
            reduceDemand(merchant);
        }
    }

    public void reduceDemand(@NotNull ShopTrader merchant){
        //merchant.getShop().getDemandManager().reduceDemand();
    }

    @Override
    public void tick() {
        tick += TICK_DELAY;
        /*if(shouldRestock()){
            CruxShopsPlugin.inst().log("Restocking ShopTraders...");
            restock();
            updateMenus();
        }

        if(shouldReduceDemand()){
            CruxShopsPlugin.inst().log("Reducing demands ShopTraders...");
            reduceDemand();
        }*/

        for(ShopTrader merchant : merchants){
            if(!(merchant instanceof TickedTime time)) continue;
            time.tick(tick, TICK_DELAY);
        }
        ShopTraderMenu.updateMenus();
    }

    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    public int getTickDelay() {
        return TICK_DELAY;
    }

    @NotNull
    public Map<Key, NumberProvider> getRestockValues() {
        return restockValues;
    }

    @NotNull
    public NumberProvider getDefaultRestockValue() {
        return defaultRestockValue;
    }

    public int getTick() {
        return tick;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }

    protected BukkitRunnable runnable;
    @Override
    public void started() {
        super.started();
        load();
        runnable = new BukkitRunnable(){
            @Override
            public void run() {
                if(shouldStop()){
                    cancel();
                    return;
                }
                tick();
            }
        };
        runnable.runTaskTimer(plugin, 0L, TICK_DELAY);
    }

    @Override
    public void stopped() {
        super.stopped();
        save();
        if(runnable != null){
            runnable.cancel();
            runnable = null;
        }
    }

    @Override
    public void save(@NotNull Plugin plugin) {
        CruxJson cfg = new CruxJson(plugin, "data/market");
        cfg.serialize("tick", tick);
        cfg.save();
    }

    @Override
    public void load(@NotNull Plugin plugin) {
        CruxJson cfg = new CruxJson(plugin, "data/market");
        tick = cfg.deserializeOrDefault("tick", Integer.class, 0);
        cfg.close();
    }

    @Override
    public void save() {
        save(plugin);
    }

    @Override
    public void load() {
        load(plugin);
    }
}
