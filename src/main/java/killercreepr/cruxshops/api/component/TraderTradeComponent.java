package killercreepr.cruxshops.api.component;

import org.jetbrains.annotations.Contract;

public interface TraderTradeComponent {
    @Contract(pure = true)
    TraderTradeComponent createCopy();
}
