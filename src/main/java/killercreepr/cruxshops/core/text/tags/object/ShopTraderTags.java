package killercreepr.cruxshops.core.text.tags.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopTraderTags implements ObjectTag<ShopTrader> {
    @NotNull
    @Override
    public Class<ShopTrader> getObjectType() {
        return ShopTrader.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("shop_trader_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull ShopTrader object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("id", (args, ctx) ->{
                return object instanceof Keyed k ? Crux.keyMinimalString(k.key()) : "unknown";
            }))
            ;
    }
}
