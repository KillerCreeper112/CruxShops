package killercreepr.cruxshops.core.config.handler;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileShopTrader implements FileObjectHandler<ShopTrader> {
    protected final MappedRegistry<Key, FileObjectHandler<? extends ShopTrader>> types = new SimpleMappedRegistry<>();
    public void registerType(Key key, FileObjectHandler<? extends ShopTrader> type){
        types.register(key, type);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull ShopTrader result) {
        return null;
    }

    @Nullable
    @Override
    public ShopTrader deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        var reg = ctx.getRegistry();
        if(!(e instanceof FileObject o)) return null;
        Key typeKey = reg.deserializeFromFile(Key.class, o.get("type"));
        if(typeKey == null) typeKey = Crux.key("simple");
        var handler = types.get(typeKey);
        if(handler == null){
            Crux.logError("ShopTrader " + typeKey + " not found!");
            return null;
        }
        return handler.deserializeFromFile(ctx, e);
    }
}
