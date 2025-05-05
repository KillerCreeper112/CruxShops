package killercreepr.cruxshops.core.text.tags.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopTradeTags implements ObjectTag<ShopTrade> {
    @NotNull
    @Override
    public Class<ShopTrade> getObjectType() {
        return ShopTrade.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("shop_trade_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull ShopTrade object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("result_amount", (args, ctx) ->{
                int index = (int) CruxMath.evaluate(ctx.deserializeString(args.getOrDefault(0, "0")));
                return object.getResults().get(index).getAmount() + "";
            }))
            .add(Tag.string("ingredient_amount", (args, ctx) ->{
                int index = (int) CruxMath.evaluate(ctx.deserializeString(args.getOrDefault(0, "0")));
                return object.getIngredients().get(index).getAmount() + "";
            }))
            ;
    }
}
