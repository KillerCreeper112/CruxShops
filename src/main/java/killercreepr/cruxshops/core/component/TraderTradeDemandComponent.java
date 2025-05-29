package killercreepr.cruxshops.core.component;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxshops.api.component.TraderTradeComponent;
import killercreepr.cruxshops.api.data.OriginalHolder;
import killercreepr.cruxshops.api.shop.trade.*;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TraderTradeDemandComponent implements TraderTradeComponent, Keyed {
    protected final Key key;
    protected int demand;
    protected int supply;

    public TraderTradeDemandComponent(Key key) {
        this.key = key;
    }

    public static TraderTradeDemandComponent load(@NotNull FileContext<?> ctx, @NotNull ShopTrader trader, FileElement element){
        if(!(element instanceof FileObject o)) return null;
        Key key = ctx.getRegistry().deserializeFromFile(Key.class, o.get("key"));
        Integer demand = o.getObject(Integer.class, "demand");
        Integer supply = o.getObject(Integer.class, "supply");
        if(demand == null || supply == null) return null;

        Integer cacheDemand = o.getObject(Integer.class, "cache_demand");
        Integer cacheSupply = o.getObject(Integer.class, "cache_supply");

        if(cacheDemand != null && cacheSupply != null){
            CacheTraderTradeDemandComponent data = new CacheTraderTradeDemandComponent(key);
            data.setSupply(supply);
            data.setDemand(demand);
            data.setCacheSupply(cacheSupply);
            data.setCacheDemand(cacheDemand);
            return data;
        }

        TraderTradeDemandComponent data = new TraderTradeDemandComponent(key);
        data.setSupply(supply);
        data.setDemand(demand);
        return data;
    }

    @Nullable
    @Override
    public FileElement save(@NotNull FileContext<?> ctx, @NotNull ShopTrader trader, @NotNull TraderTrade trade) {
        return new FileObject()
            .add("key", ctx.getRegistry().serializeToFile(key))
            .addProperty("demand", demand)
            .addProperty("supply", supply);
    }

    public ShopTrade adjustTrade(TraderTrade traderTrade, ShopTrade trade, ShopTraderDemandComponent.TradeModifier modifier){
        TradeType type = trade.equals(traderTrade.getSellingTrade()) ? TradeType.SELL : TradeType.BUY;

        switch (type){
            case BUY -> {
                List<ShopTradeIngredient> ingredients = new ArrayList<>();
                trade.getIngredients().forEach(ingredient -> {
                    if(modifier.canAdjust(ingredient)){
                        ingredient = adjustIngredient(ingredient, modifier, type);
                    }
                    if(ingredient != null) ingredients.add(ingredient);
                });
                return trade.withIngredients(ingredients);
            }
            case SELL -> {
                List<ShopTradeResult> results = new ArrayList<>();
                trade.getResults().forEach(ingredient -> {
                    if(modifier.canAdjust(ingredient)){
                        ingredient = adjustResult(ingredient, modifier, type);
                    }
                    if(ingredient != null) results.add(ingredient);
                });
                return trade.withResults(results);
            }
        }

        return trade;
    }

    protected double getBuyModifier(int demand, int supply, double sensitivity, double min, double max) {
        // Higher demand increases price, more supply reduces it
        double modifier = 1.0 + (demand - supply) * sensitivity;
        return Math.max(min, Math.min(modifier, max));
    }

    protected double getSellModifier(int demand, int supply, double sensitivity, double min, double max) {
        // More supply decreases price, higher demand increases it — but reversed effect for sellers
        double modifier = 1.0 - (supply - demand) * sensitivity;
        return Math.max(min, Math.min(modifier, max));
    }


    public ShopTradeObject adjustObject(ShopTradeObject result, ShopTraderDemandComponent.TradeModifier mod, TradeType type) {
        int baseAmount = result.getAmount();
        double modifier;

        if (type == TradeType.BUY) {
            modifier = getBuyModifier(demand, supply, mod.getBuyModifier(), 0.01, 5);
        } else {
            modifier = getSellModifier(demand, supply, mod.getSellModifier(), 0.01, 5);
        }

        int adjustedAmount = (int) Math.round(baseAmount * modifier);
        adjustedAmount = mod.clampPrice(adjustedAmount, OriginalHolder.getCompleteOriginalOrThis(result).getAmount());

        return result.withAmount(Math.max(adjustedAmount, 1));
    }

    @NotNull
    @Override
    public Key key() {
        return key;
    }

    public enum TradeType {
        BUY,
        SELL
    }

    public ShopTradeResult adjustResult(ShopTradeResult result, ShopTraderDemandComponent.TradeModifier mod,
                                        TradeType type) {
        return (ShopTradeResult) adjustObject(result, mod, type);
    }

    public ShopTradeIngredient adjustIngredient(ShopTradeIngredient ingredient, ShopTraderDemandComponent.TradeModifier mod,
                                                TradeType type) {
        return (ShopTradeIngredient) adjustObject(ingredient, mod, type);
    }


    public int addDemand(int amount){
        setDemand(demand+amount);
        return demand;
    }
    public int addSupply(int amount){
        setSupply(supply+amount);
        return supply;
    }

    public int subtractDemand(int amount){
        setDemand(demand-amount);
        return demand;
    }
    public int subtractSupply(int amount){
        setSupply(supply-amount);
        return supply;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = Math.max(demand, 0);
    }

    public int getSupply() {
        return supply;
    }

    public void setSupply(int supply) {
        this.supply = Math.max(supply, 0);
    }

    @Override
    public TraderTradeComponent createCopy() {
        TraderTradeDemandComponent data = new TraderTradeDemandComponent(key);
        data.setDemand(demand);
        data.setSupply(supply);
        return data;
    }
}
