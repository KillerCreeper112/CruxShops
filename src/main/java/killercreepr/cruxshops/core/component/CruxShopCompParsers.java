package killercreepr.cruxshops.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;

public class CruxShopCompParsers {
    public static final PersistTextParser<ShopTraderDemandComponent.TradeModifier> DEMAND_TRADE_MODIFIER =
        PersistTextParser.mapBuilder(ShopTraderDemandComponent.TradeModifier.class)
            .field("modifier", TextInputField.field(PersistTextParser.DOUBLE, ShopTraderDemandComponent.TradeModifier::getModifier))
            .field("price_clamp_min", TextInputField.field(PersistTextParser.INTEGER, ShopTraderDemandComponent.TradeModifier::getPriceClampMin))
            .field("price_clamp_max", TextInputField.field(PersistTextParser.INTEGER, ShopTraderDemandComponent.TradeModifier::getPriceClampMax))
            .apply(ctx ->{
                double modifier = ctx.getOptional("modifier", 1D);
                Integer priceMin = ctx.getOptional("price_clamp_min");
                Integer priceMax = ctx.getOptional("price_clamp_max");
                return new ShopTraderDemandComponent.TradeModifier(modifier, priceMin, priceMax);
            });

    public static final PersistTextParser<ShopTraderDemandComponent> SHOP_TRADER_DEMAND =
        PersistTextParser.mapBuilder(ShopTraderDemandComponent.class)
            .field("buying_modifier", TextInputField.field(DEMAND_TRADE_MODIFIER, ShopTraderDemandComponent::getBuyingModifier))
            .field("selling_modifier", TextInputField.field(DEMAND_TRADE_MODIFIER, ShopTraderDemandComponent::getSellingModifier))
            .apply(ctx ->{
                return new ShopTraderDemandComponent(
                    ctx.getOptional("buying_modifier"),
                    ctx.getOptional("selling_modifier")
                );
            });
}
