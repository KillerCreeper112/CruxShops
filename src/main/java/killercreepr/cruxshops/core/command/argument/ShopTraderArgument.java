package killercreepr.cruxshops.core.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.core.command.argument.CruxKeyedArgument;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.registries.ShopsRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ShopTraderArgument implements CruxKeyedArgument<ShopTrader> {

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(var trader : ShopsRegistries.SHOP_TRADER.values()){
            if(!(trader instanceof Keyed k)) continue;
            builder.suggest(k.key().asString());
        }
        return builder.buildFuture();
    }

    @NotNull
    @Override
    public ShopTrader parse(@NotNull Key key) {
        return Objects.requireNonNull(
            ShopsRegistries.SHOP_TRADER.get(key), "ShopTrader of " + key + " not found!"
        );
    }
}
