package killercreepr.cruxshops.core.values;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ValuesProvider extends Reloadable {
    @Nullable
    DynamicItem MERCHANT_SHOP_TRADE_ITEM();
    @Nullable
    DynamicItem MERCHANT_SHOP_TRADE_ITEM_SELLING();
    @Nullable
    DynamicItem MERCHANT_SHOP_TRADE_ITEM_BUYER_LIMITED();
    @Nullable
    DynamicItem MERCHANT_SHOP_TRADE_ITEM_SELLING_BUYER_LIMITED();
    Holder<List<String>> BUYING_FOR_LINES();
    Holder<List<String>> BUYING_FOR_LINES_MULTIPLIER();
    Holder<List<String>> SELLING_FOR_LINES();
    Holder<List<String>> SELLING_FOR_LINES_MULTIPLIER();
    Holder<List<String>> DEMAND_PRICE_HIGHER();
    Holder<List<String>> DEMAND_PRICE_LOWER();
    Holder<List<String>> DEMAND_PRICE_HIGHER_SELLING();
    Holder<List<String>> DEMAND_PRICE_LOWER_SELLING();
    Holder<NumberProvider> DEMAND_CHANGE_AMOUNT();
    Holder<NumberProvider> DEMAND_CHANGE_SKIP_CHANCE();
}
