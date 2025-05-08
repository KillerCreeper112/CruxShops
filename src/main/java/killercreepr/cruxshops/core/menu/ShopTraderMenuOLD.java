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

@Deprecated
public class ShopTraderMenuOLD extends ConfigMenu {
    protected final @NotNull ShopTrader trader;
    public ShopTraderMenuOLD(@NotNull MenuHolder holder, @NotNull DataExchange info) {
        this(holder, info, null);
    }

    public ShopTraderMenuOLD(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags) {
        super(holder, info, tags);
        this.trader = info.getWithDefault("trader", ShopTrader.class, i -> i.getOrThrow(ShopTrader.class));
    }

    @NotNull
    @Override
    public MergedTagContainer buildTags(@NotNull TagParser tagParser) {
        MergedTagContainer tags = super.buildTags(tagParser);
        if(trader==null) return tags;
        tags.add(Tag.parsed("trade_slots", buildTradeSlotsTitle()))
            .add(Tag.parsed("page", CruxMath.format(page+1)))
            .add(Tag.parsed("max_page", CruxMath.format(getMaxPage()+1)))
        ;
        return tags;
    }

    public int getMaxPage(){
        return Math.max((int) Math.ceil((float) trades.size()/(float)MAX) - 1, 0);
    }

    protected List<TraderTrade> trades = new ArrayList<>();
    @Override
    public void load() {
        var viewer = info().getOrThrow("viewer", Entity.class);
        trades.clear();
        trades.addAll(trader.getTrades(viewer));

        super.load();

        openPage(page);
        setupPageButtons();
    }

    protected final int[] backPageIndexes = new int[]{
        9, 17, 18, 26
    };
    protected final int[] nextPageIndexes = new int[]{
        27, 35, 36, 44
    };
    public Slot buildBackSlot(int index){
        return new SimpleFixedSlot(this, index){
            @Override
            public void onClick(@NotNull HumanEntity player, @NotNull InventoryClickEvent event) {
                if(!(player instanceof Player p)) return;
                int newPage = CruxMath.wrap(page-1, 0, getMaxPage());
                if(newPage==page) return;
                page = newPage;
                clearItems(true);
                clearMenuItems(true);
                load();
                open(p);
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

    public void setupPageButtons(){
        for(int index : backPageIndexes){
            addSlot(buildBackSlot(index), true);
        }

        for(int index : nextPageIndexes){
            addSlot(buildNextSlot(index), true);
        }
    }

    public Slot buildNextSlot(int index){
        return new SimpleFixedSlot(this, index){
            @Override
            public void onClick(@NotNull HumanEntity player, @NotNull InventoryClickEvent event) {
                if(!(player instanceof Player p)) return;
                int newPage = CruxMath.wrap(page+1, 0, getMaxPage());
                if(newPage==page) return;
                page = newPage;
                clearItems(true);
                clearMenuItems(true);
                load();
                open(p);
                CreateSound.sound(Sound.UI_BUTTON_CLICK).playFor(p);
            }

            @Override
            public @Nullable ItemStack getSlottedItemReplacement() {
                return CruxItem.create(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                    .itemModel(Crux.key("gui/nothing"))
                    .itemName("Next").item();
            }
        };
    }

    public boolean canUse(ShopTrade trade, Entity e){
        return trade != null && trader.canPurchaseTrade(e, trade) == ShopTrader.CanPurchase.TRUE;
    }

    public @NotNull String buildTradeSlotsTitle(){
        Entity viewer = this.info.getOrThrow("viewer", Entity.class);
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for(var trade : getTradesInPage(page)){
            index++;

            String symbol = convertNumber(index+1);

            if(!canUse(trade.getBuyingTrade(), viewer) && !canUse(trade.getSellingTrade(), viewer)){
                symbol = "<red>" + symbol + "</red>";
            }

            if(index == 1){
                builder.append("<crux_space:-146><font:\"crux:shop\">")
                    .append(symbol);
                continue;
            }
            String space;
            if(index % 2 == 0){
                space = "<cruxshop/trade/2>";
            }else{
                space = "<cruxshop/trade/else>";
            }

            builder.append(space).append("<font:\"crux:shop\">")
                .append(symbol);
        }
        return builder.toString();
    }

    public static String convertNumber(int n) {
        if (n >= 0 && n <= 9) {
            return Integer.toString(n);
        } else if (n >= 10 && n <= 35) {
            // 'a' is 97 in ASCII, so 10 -> 'a', 11 -> 'b', ..., 35 -> 'z'
            char letter = (char) ('a' + (n - 10));
            return Character.toString(letter);
        } else {
            throw new IllegalArgumentException("Number out of range (must be 0–35)");
        }
    }

    public List<TraderTrade> getTradesInPage(int page){
        List<TraderTrade> current = new ArrayList<>();
        //var viewer = info().getOrThrow("viewer", Entity.class);
        for(int i = 0; i < MAX; i++){
            int listIndex = i + (page * MAX);
            if(listIndex >= trades.size()) break;
            var trade = trades.get(listIndex);
            current.add(trade);
        }
        return current;
    }

    public Config cfg(){
        return CruxShopsPlugin.inst().values();
    }

    public CruxItem applySellingItem(CruxItem item, ShopTrade trade, TraderTrade traderTrade){
        Config cfg = cfg();
        DynamicItem displayItem = cfg.SELLING_ITEM.value();
        if(displayItem == null) return item;
        MergedTagContainer tags = trader.buildTags(trade);
        if(tags == null) tags = TagContainer.merged();
        tags.hook(trade).hook(traderTrade);
        tags.add(Tag.string("can_use_trade", (args, ctx) ->{
            var entity = info().getOrThrow("viewer", Entity.class);
            return canUse(trade, entity) + "";
        }));
        return displayItem.applyComponents(item, TextParserContext.builder().tags(tags).build());
    }

    public CruxItem applyBuyingItem(CruxItem item, ShopTrade trade, TraderTrade traderTrade){
        Config cfg = cfg();
        DynamicItem displayItem = cfg.BUYING_ITEM.value();
        if(displayItem == null) return item;
        MergedTagContainer tags = trader.buildTags(trade);
        if(tags == null) tags = TagContainer.merged();
        tags.hook(trade).hook(traderTrade);
        tags.add(Tag.string("can_use_trade", (args, ctx) ->{
            var entity = info().getOrThrow("viewer", Entity.class);
            return canUse(trade, entity) + "";
        }));
        return displayItem.applyComponents(item, TextParserContext.builder().tags(tags).build());
    }

    public CruxItem applyResultItem(CruxItem item, ShopTrade trade, TraderTrade traderTrade){
        Config cfg = cfg();
        DynamicItem displayItem = null;//cfg.RESULT_ITEM.value();
        if(displayItem == null) return item;
        MergedTagContainer tags = trader.buildTags(trade);
        if(tags == null) tags = TagContainer.merged();
        tags.hook(trade).hook(traderTrade);
        tags.add(Tag.string("can_use_trade", (args, ctx) ->{
            var entity = info().getOrThrow("viewer", Entity.class);
            return canUse(trade, entity) + "";
        }));
        return displayItem.applyComponents(item, TextParserContext.builder().tags(tags).build());
    }

    protected int page = 0;
    protected final int MAX = 12;
    public void openPage(int page){
        List<TraderTrade> trades = getTradesInPage(page);
        for(int i = 0; i < MAX; i++){
            int listIndex = i + (page * MAX);
            if(listIndex >= trades.size()) break;
            TraderTrade trade = trades.get(listIndex);
            setTrade(i, trade);
        }
    }

    public BiConsumer<HumanEntity, InventoryClickEvent> buildBuyingClick(TraderTrade trade){
        return purchaseTradeConsumer(trade::getBuyingTrade, trade);
    }

    public BiConsumer<HumanEntity, InventoryClickEvent> buildResultClick(TraderTrade trade){
        return (p, event) ->{
            //p.sendMessage("todo");
        };
    }

    public BiConsumer<HumanEntity, InventoryClickEvent> purchaseTradeConsumer(Holder<ShopTrade> holder, TraderTrade traderTrade){
        return (p, event) ->{
            var t = holder.value();
            if(t == null) return;
            if(trader.canPurchaseTrade(p, t) != ShopTrader.CanPurchase.TRUE){
                return;
            }
            /*if(!t.canView(p)){
                p.sendMessage("Can't view");
                return;
            }
            if(!t.canAfford(p)){
                p.sendMessage("Can't afford");
                return;
            }
            if(!t.canAccept(p)){
                p.sendMessage("Can't accept");
                return;
            }*/

            if(trader.purchaseTrade(p, traderTrade, t)){
                clearItems(true);
                clearMenuItems(true);
                load();
                open(p);
            }
            //p.sendMessage("purchase");
        };
    }

    public BiConsumer<HumanEntity, InventoryClickEvent> buildSellingClick(TraderTrade trade){
        return purchaseTradeConsumer(trade::getSellingTrade, trade);
    }

    public Map<Integer, Pair<CruxItem, BiConsumer<HumanEntity, InventoryClickEvent>>> buildTradeItems(TraderTrade trade){
        Map<Integer, Pair<CruxItem, BiConsumer<HumanEntity, InventoryClickEvent>>> map = new HashMap<>();
        //0, 1, 2
        ShopTrade mainTrade = null;
        ItemStack icon = null;
        var buying = trade.getBuyingTrade();
        if(buying == null) map.put(0, null);
        else{
            mainTrade = buying;
            icon = mainTrade.getResults().getFirst().buildIcon();
            ShopTradeIngredient ingredient = buying.getIngredients().getFirst();
            map.put(0, new Pair<>(applyBuyingItem(applyItem(icon, ingredient.buildIcon()), buying, trade), buildBuyingClick(trade)));
        }

        var selling = trade.getSellingTrade();
        if(selling == null) map.put(2, null);
        else{
            if(mainTrade == null){
                mainTrade = selling;
                icon = mainTrade.getIngredients().getFirst().buildIcon();
            }
            ShopTradeResult result = selling.getResults().getFirst();
            map.put(2, new Pair<>(applySellingItem(applyItem(icon, result.buildIcon()), selling, trade), buildSellingClick(trade)));
        }
        if(mainTrade != null){
            map.put(1, new Pair<>(applyResultItem(CruxItem.wrap(icon), selling, trade), buildResultClick(trade)));
        }
        return map;
    }

    public CruxItem applyItem(ItemStack icon, ItemStack ingredient){
        icon = icon.clone();
        ItemStack finalIcon = icon;
        ingredient.editMeta(meta ->{
            finalIcon.editMeta(iconMeta ->{
                if(meta.hasItemModel()){
                    iconMeta.setItemModel(meta.getItemModel());
                }else iconMeta.setItemModel(null);
                iconMeta.setMaxStackSize(CruxItem.getMaxStackSize(ingredient));
            });
        });
        icon.setAmount(ingredient.getAmount());
        return CruxItem.wrap(icon);
    }

    public int getStartingInvSlotFromTradeIndex(int index){
        switch (index){
            case 0 -> { return 1; }
            case 1 -> { return 5; }

            case 2 -> { return 10; }
            case 3 -> { return 14; }

            case 4 -> { return 19; }
            case 5 -> { return 23; }

            case 6 -> { return 28; }
            case 7 -> { return 32; }

            case 8 -> { return 37; }
            case 9 -> { return 41; }

            case 10 -> { return 46; }
            case 11 -> { return 50; }
        }
        throw new IndexOutOfBoundsException(index + " out of bounds, must be 0-11");
    }

    public void setTrade(int index, TraderTrade trade){
        int startIndex = getStartingInvSlotFromTradeIndex(index);
        buildTradeItems(trade).forEach((tradeIndexSlot, action) ->{
            int invSlot = startIndex + tradeIndexSlot;
            if(action == null){
                setItem(invSlot, null);
                getSlots().remove(invSlot);
                return;
            }
            setItem(invSlot, action.getFirst().item(), new SimpleFixedSlot(this, invSlot){
                @Override
                public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                    action.getSecond().accept(p, event);
                }
            });
        });
    }
}
