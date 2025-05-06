package killercreepr.cruxshops.core.text.tags.object;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TraderTradeTags implements SimpleObjectTag<TraderTrade> {
    @NotNull
    @Override
    public Class<TraderTrade> getObjectType() {
        return TraderTrade.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("trader_trade_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull TraderTrade object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("has_component", (args, ctx) ->{
                String id = ctx.deserializeString(args.get(0));
                DataComponentType<?> type = CruxRegistries.DATA_COMPONENT_TYPE.get(Crux.key(id));
                if(type == null) return id + " data component not found";
                return object.has(type) + "";
            }))
            ;
    }

    @Override
    public @Nullable Map<Object, FormatPrefix> hookObjects(TraderTrade object) {
        Map<Object, FormatPrefix> map = new HashMap<>();
        if(object.getBuyingTrade() != null){
            map.put(object.getBuyingTrade(), FormatPrefix.simple("buying/"));
        }
        if(object.getSellingTrade() != null){
            map.put(object.getSellingTrade(), FormatPrefix.simple("selling/"));
        }
        return map;
    }
}
