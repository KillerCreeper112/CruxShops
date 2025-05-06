package killercreepr.cruxshops.core.menu;

import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.util.Pair;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.slot.Slot;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.CruxShopsPlugin;
import killercreepr.cruxshops.core.config.Config;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ShopTraderSelectMenu extends ConfigMenu {
    protected final @NotNull ShopTrader trader;
    protected final @NotNull TraderTrade trade;
    public ShopTraderSelectMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, TraderTrade trade) {
        this(holder, info, null, trade);
    }

    public ShopTraderSelectMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags, @NotNull TraderTrade trade) {
        super(holder, info, tags);
        this.trader = info.getWithDefault("trader", ShopTrader.class, i -> i.getOrThrow(ShopTrader.class));
        this.trade = trade;
    }

    @NotNull
    @Override
    public MergedTagContainer buildTags(@NotNull TagParser tagParser) {
        MergedTagContainer tags = super.buildTags(tagParser);
        if(trader==null) return tags;
        tags.add(Tag.parsed("trade_slots", buildTradeSlotsTitle()))
        ;
        return tags;
    }

    @Override
    public void load() {
        var viewer = info().getOrThrow("viewer", Entity.class);
        trades.clear();
        trades.addAll(trader.getTrades(viewer));

        super.load();

        setupPageButtons();
    }

    public void setupPageButtons(){
        buildBackSlot(info().getOrThrow("back_index", Number.class).intValue());
    }

    public Slot buildBackSlot(int index){
        return new SimpleFixedSlot(this, index){
            @Override
            public void onClick(@NotNull HumanEntity player, @NotNull InventoryClickEvent event) {
                if(!(player instanceof Player p)) return;
                CreateSound.sound(Sound.UI_BUTTON_CLICK).playFor(p);
            }

            @Override
            public @Nullable ItemStack getSlottedItemReplacement() {
                return CruxItem.create(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                    .itemModel(Crux.key("gui/nothing"))
                    .itemName("Back").item();
            }
        };
    }

    public boolean canUse(ShopTrade trade, Entity e){
        return trade != null && trader.canPurchaseTrade(e, trade) == ShopTrader.CanPurchase.TRUE;
    }

    public @NotNull String buildTradeSlotsTitle(){
        Entity viewer = this.info.getOrThrow("viewer", Entity.class);
        StringBuilder builder = new StringBuilder("<cruxshop/trade_select/start>");
        ShopTrade trade = this.trade.getBuyingTrade();
        String value;
        if(trade == null){
            value = "2";
        }else{
            value = "1";
            if(!canUse(trade, viewer)){
                value = "<red>" + value + "</red>";
            }
        }
        builder.append("<cruxshop/trade_select/buy>").append(value);

        trade = this.trade.getSellingTrade();
        if(trade == null){
            value = "2";
        }else{
            value = "1";
            if(!canUse(trade, viewer)){
                value = "<red>" + value + "</red>";
            }
        }
        builder.append("<cruxshop/trade_select/sell>").append(value);
        return builder.toString();
    }

    public Config cfg(){
        return CruxShopsPlugin.inst().values();
    }
}
