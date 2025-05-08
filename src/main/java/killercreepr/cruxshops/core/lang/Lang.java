package killercreepr.cruxshops.core.lang;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.communication.lang.Msg;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Lang {
    public static final Holder<CreateLang> CLAIM_LANG = Lang::lang;

    private static CreateLang lang;
    public static CreateLang lang(){
        return lang;
    }
    public static CreateLang setLang(CreateLang l){
        lang = l;
        return l;
    }

    public static final Msg TRADE_CANNOT_AFFORD = create("trade_cannot_afford",
        Communicator.builder()
            .chat("<red>You do not have the required ingredients for this trade.")
            .sound(CreateSound.sound(Sound.ENTITY_VILLAGER_NO))
            .build()
    );

    public static final Msg TRADE_NOT_ENOUGH_STOCK = create("trade_not_enough_stock",
        Communicator.builder()
            .chat("<red>This trade does not have enough stock.")
            .sound(CreateSound.sound(Sound.ENTITY_VILLAGER_NO))
            .build()
    );

    public static final Msg TRADE_CANNOT_ACCEPT = create("trade_cannot_accept",
        Communicator.builder()
            .chat("<red>You do not have enough inventory space.")
            .sound(CreateSound.sound(Sound.ENTITY_VILLAGER_NO))
            .build()
    );

    public static final Msg TRADE_PURCHASE_BUYING = create(
        Communicator.builder()
            .chat("<yellow><shop_trade_result_amount:0>x <shop_trade_result_name:0> sold for <shop_trade_ingredient_amount:0>x <shop_trade_ingredient_name:0>")
            .sound(CreateSound.sound(Sound.BLOCK_NOTE_BLOCK_PLING, 2f))
            .build()
    );

    public static final Msg TRADE_PURCHASE_SELLING = create(
        Communicator.builder()
            .chat("<yellow><shop_trade_ingredient_amount:0>x <shop_trade_ingredient_name:0> sold for <shop_trade_result_amount:0>x <shop_trade_result_name:0>")
            .sound(CreateSound.sound(Sound.BLOCK_NOTE_BLOCK_PLING, 2f))
            .build()
    );

    public static Msg create(@NotNull String id, @Nullable Communicator msg){
        return new Msg(id, msg, CLAIM_LANG);
    }

    public static Msg create(@Nullable Communicator msg){
        return new Msg(msg, CLAIM_LANG);
    }
}
