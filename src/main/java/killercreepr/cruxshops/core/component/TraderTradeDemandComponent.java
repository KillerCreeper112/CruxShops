package killercreepr.cruxshops.core.component;

import killercreepr.cruxshops.api.component.TraderTradeComponent;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeObject;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;

import java.util.ArrayList;
import java.util.List;

public class TraderTradeDemandComponent implements TraderTradeComponent {
    protected int demand;
    protected int supply;

    public ShopTrade adjustTrade(ShopTrade trade, ShopTraderDemandComponent.TradeModifier modifier){
        List<ShopTradeIngredient> ingredients = new ArrayList<>();
        List<ShopTradeResult> results = new ArrayList<>();

        trade.getIngredients().forEach(ingredient -> {
            if(modifier.canAdjust(ingredient)){
                ingredient = adjustIngredient(ingredient, modifier);
            }
            if(ingredient != null) ingredients.add(ingredient);
        });

        trade.getResults().forEach(ingredient -> {
            if(modifier.canAdjust(ingredient)){
                ingredient = adjustResult(ingredient, modifier);
            }
            if(ingredient != null) results.add(ingredient);
        });

        return trade.withIngredients(ingredients).withResults(results);
    }

    protected double getPriceModifier(int demand, int supply, double sensitivity, double min, double max) {
        double modifier = 1.0 + (demand - supply) * sensitivity;
        return Math.max(min, Math.min(modifier, max));
    }


    public ShopTradeObject adjustObject(ShopTradeObject result, ShopTraderDemandComponent.TradeModifier mod) {
        int baseAmount = result.getAmount();
        double modifier = getPriceModifier(demand, supply, mod.getModifier(), 0.01, 5);
        int adjustedAmount = (int) Math.round(baseAmount * modifier);

        var clamp = mod.getPriceClamp();
        if(clamp != null){

        }

        return result.withAmount(adjustedAmount);
    }

    public ShopTradeResult adjustResult(ShopTradeResult result, ShopTraderDemandComponent.TradeModifier mod) {
        return (ShopTradeResult) adjustObject(result, mod);
    }

    public ShopTradeIngredient adjustIngredient(ShopTradeIngredient ingredient, ShopTraderDemandComponent.TradeModifier mod) {
        return (ShopTradeIngredient) adjustObject(ingredient, mod);
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
        TraderTradeDemandComponent data = new TraderTradeDemandComponent();
        data.setDemand(demand);
        data.setSupply(supply);
        return data;
    }
}
