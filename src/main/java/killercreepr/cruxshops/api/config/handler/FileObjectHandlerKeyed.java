package killercreepr.cruxshops.api.config.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileObjectHandlerKeyed<T> extends FileObjectHandler<T> {
    @Nullable T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key);
}
