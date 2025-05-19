package killercreepr.cruxshops.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;

public class CruxShopCompParsers {
    public static final PersistTextParser<ShopTraderDemandComponent.TradeModifier> DEMAND_TRADE_MODIFIER =
        PersistTextParser.mapBuilder(ShopTraderDemandComponent.TradeModifier.class)
            .field("buy_modifier", TextInputField.field(PersistTextParser.DOUBLE, ShopTraderDemandComponent.TradeModifier::getBuyModifier))
            .field("sell_modifier", TextInputField.field(PersistTextParser.DOUBLE, ShopTraderDemandComponent.TradeModifier::getSellModifier))
            .field("price_clamp_min", TextInputField.field(PersistTextParser.INTEGER, ShopTraderDemandComponent.TradeModifier::getPriceClampMin))
            .field("price_clamp_max", TextInputField.field(PersistTextParser.INTEGER, ShopTraderDemandComponent.TradeModifier::getPriceClampMax))
            .apply(ctx ->{
                double modifier = ctx.getOptional("modifier", 1D);
                double sellModifier = ctx.getOptional("sell_modifier", modifier);
                double buyModifier = ctx.getOptional("buy_modifier", modifier);
                Integer priceMin = ctx.getOptional("price_clamp_min");
                Integer priceMax = ctx.getOptional("price_clamp_max");
                return new ShopTraderDemandComponent.TradeModifier(buyModifier, sellModifier, priceMin, priceMax);
            });

    public static final PersistTextParser<ShopTraderDemandComponent> SHOP_TRADER_DEMAND =
        PersistTextParser.mapBuilder(ShopTraderDemandComponent.class)
            .field("buying_modifier", TextInputField.field(DEMAND_TRADE_MODIFIER, ShopTraderDemandComponent::getBuyingModifier))
            .field("selling_modifier", TextInputField.field(DEMAND_TRADE_MODIFIER, ShopTraderDemandComponent::getSellingModifier))

            .field("demand_tick", TextInputField.field(ComponentInputParsers.NUMBER_PROVIDER, ShopTraderDemandComponent::getDemandTick))

            .field("demand_add", TextInputField.field(ComponentInputParsers.NUMBER_PROVIDER, ShopTraderDemandComponent::getDemandAdd))
            .field("supply_add", TextInputField.field(ComponentInputParsers.NUMBER_PROVIDER, ShopTraderDemandComponent::getSupplyAdd))

            .field("demand_multiplier", TextInputField.field(ComponentInputParsers.NUMBER_PROVIDER, ShopTraderDemandComponent::getDemandMultiplier))
            .field("supply_multiplier", TextInputField.field(ComponentInputParsers.NUMBER_PROVIDER, ShopTraderDemandComponent::getSupplyMultiplier))
            .apply(ctx ->{
                return new ShopTraderDemandComponent(
                    ctx.getOptional("demand_tick"),
                    ctx.getOptional("demand_add"),
                    ctx.getOptional("supply_add"),

                    ctx.getOptional("demand_multiplier"),
                    ctx.getOptional("supply_multiplier"),

                    ctx.getOptional("buying_modifier"),
                    ctx.getOptional("selling_modifier")
                );
            });
}
