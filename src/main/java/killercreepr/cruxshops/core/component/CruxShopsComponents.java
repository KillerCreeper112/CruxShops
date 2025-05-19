package killercreepr.cruxshops.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;

import java.util.function.UnaryOperator;

public class CruxShopsComponents {
    public static void register(){}
    public static final DataComponentType<ShopTraderDemandComponent> SHOP_TRADER_DEMAND = register("shop_trader/demand", builder ->
        builder.textParser(CruxShopCompParsers.SHOP_TRADER_DEMAND));

    public static final DataComponentType<TraderTradeDemandComponent> TRADER_TRADE_DEMAND = register("trader_trade/demand", builder ->
        builder.textParser(PersistTextParser.mapBuilder(TraderTradeDemandComponent.class)
                .field("key", TextInputField.field(PersistTextParser.KEY, TraderTradeDemandComponent::key))
            .apply(ctx ->{
                boolean cache = ctx.getOptional("cache", true);
                if(cache) return new CacheTraderTradeDemandComponent(ctx.get("key"));
                return new TraderTradeDemandComponent(ctx.get("key"));
            })));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
