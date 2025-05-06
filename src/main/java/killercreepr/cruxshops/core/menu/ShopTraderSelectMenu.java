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
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.slot.Slot;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import killercreepr.cruxshops.api.data.OriginalHolder;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.CruxShopsPlugin;
import killercreepr.cruxshops.core.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public class ShopTraderSelectMenu extends ConfigMenu {
    protected final @NotNull ShopTrader trader;
    protected @NotNull TraderTrade trade;

    public ShopTraderSelectMenu(@NotNull MenuHolder holder, @NotNull DataExchange info) {
        this(holder, info, null);
    }

    public ShopTraderSelectMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags) {
        super(holder, info, tags);
        this.trader = info.getWithDefault("trader", ShopTrader.class, i -> i.getOrThrow(ShopTrader.class));
        this.trade = info.getWithDefault("trade", TraderTrade.class, i -> i.getOrThrow(TraderTrade.class));
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
    public BiConsumer<HumanEntity, InventoryClickEvent> buildSellingClick(){
        return purchaseTradeConsumer(trade::getSellingTrade);
    }

    public BiConsumer<HumanEntity, InventoryClickEvent> buildBuyingClick(){
        return purchaseTradeConsumer(trade::getBuyingTrade);
    }
    public BiConsumer<HumanEntity, InventoryClickEvent> purchaseTradeConsumer(Holder<ShopTrade> holder){
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

            if(trader.purchaseTrade(p, trade, t)){
                reload();
                open(p);
            }
            p.sendMessage("purchase");
        };
    }

    public void reload(){
        clearItems(true);
        clearMenuItems(true);
        load();
    }

    public CruxItem applyName(CruxItem item, int amount){
        if(amount <= CruxItem.getMaxStackSize(item.item())) return item;
        item.editMeta(meta ->{

            ItemRarity rarity = meta.hasRarity() ? meta.getRarity() : ItemRarity.COMMON;
            meta.displayName(
                Component.empty()
                    .decoration(TextDecoration.ITALIC, false)
                    .append(
                        Component.empty().color(rarity.color())
                            .append(
                                getName(item.item())
                            )
                    )
                    .append(
                        Component.text(" x" + CruxMath.format(amount))
                            .color(NamedTextColor.WHITE)
                    )
            );
        });
        return item;
    }

    public Component getName(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta.hasDisplayName()) return meta.displayName();
        if(meta.hasItemName()) return meta.itemName();
        return Component.translatable(item.getType());
    }

    @Override
    public void load() {
        var viewer = info().getOrThrow("viewer", Entity.class);
        this.trade = this.trader.adjustTrade(viewer, OriginalHolder.getCompleteOriginalOrThis(this.trade));

        super.load();

        setupTrades();
        setupPageButtons();
    }

    public CruxItem applySellingItem(CruxItem item, ShopTrade trade, TraderTrade traderTrade){
        Config cfg = cfg();
        DynamicItem displayItem = cfg.SELLING_ITEM.value();
        applyName(item, trade.getIngredients().getFirst().getAmount());
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
        applyName(item, trade.getResults().getFirst().getAmount());
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

    public void setupTrades(){
        var trade = this.trade.getBuyingTrade();
        if(trade != null){
            var buyTrade = trade;
            ItemStack icon = trade.getResults().getFirst().buildIcon();

            var click = buildBuyingClick();
            holder.info().getOrThrow("buy_indexes", List.class).forEach(obj ->{
                setItem(
                    ((Number) (obj)).intValue(),
                    applyBuyingItem(applyItem(icon, CruxItem.create(Material.LIGHT_GRAY_STAINED_GLASS_PANE).itemModel(Crux.key("gui/nothing")).item())
                        .amount(1), buyTrade, this.trade).item(),
                    new SimpleFixedSlot(this, ((Number) (obj)).intValue()){
                        @Override
                        public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                            click.accept(p, event);
                        }
                    }
                );
            });

            setItem(
                holder.info().getOrThrow("buy_ingredient_index", Number.class).intValue(),
                applyBuyingItem(applyItem(icon, trade.getIngredients().getFirst().buildIcon()), trade, this.trade).item(),
                new SimpleFixedSlot(this, holder.info().getOrThrow("buy_ingredient_index", Number.class).intValue()){
                    @Override
                    public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                        click.accept(p, event);
                    }
                }
            );
            setItem(
                holder.info().getOrThrow("buy_result_index", Number.class).intValue(),
                applyBuyingItem(CruxItem.wrap(trade.getResults().getFirst().buildIcon()), trade, this.trade).item(),
                new SimpleFixedSlot(this, holder.info().getOrThrow("buy_result_index", Number.class).intValue()){
                    @Override
                    public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                        click.accept(p, event);
                    }
                }
            );
        }
        trade = this.trade.getSellingTrade();
        if(trade != null){
            ItemStack icon = trade.getIngredients().getFirst().buildIcon();
            var sellTrade = trade;
            var click = buildSellingClick();
            holder.info().getOrThrow("sell_indexes", List.class).forEach(obj ->{
                setItem(
                    ((Number) (obj)).intValue(),
                    applySellingItem(applyItem(icon, CruxItem.create(Material.LIGHT_GRAY_STAINED_GLASS_PANE).itemModel(Crux.key("gui/nothing")).item()).amount(1),
                        sellTrade, this.trade).item(),
                    new SimpleFixedSlot(this, ((Number) (obj)).intValue()){
                        @Override
                        public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                            click.accept(p, event);
                        }
                    }
                );
            });

            setItem(
                holder.info().getOrThrow("sell_ingredient_index", Number.class).intValue(),
                applySellingItem(CruxItem.wrap(trade.getIngredients().getFirst().buildIcon()), trade, this.trade).item(),
                new SimpleFixedSlot(this, holder.info().getOrThrow("sell_ingredient_index", Number.class).intValue()){
                    @Override
                    public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                        click.accept(p, event);
                    }
                }
            );
            setItem(
                holder.info().getOrThrow("sell_result_index", Number.class).intValue(),
                applySellingItem(applyItem(icon, trade.getResults().getFirst().buildIcon()), trade, this.trade).item(),
                new SimpleFixedSlot(this, holder.info().getOrThrow("sell_result_index", Number.class).intValue()){
                    @Override
                    public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                        click.accept(p, event);
                    }
                }
            );
        }
    }

    public void setupPageButtons(){
        addSlot(buildBackSlot(holder.info().getOrThrow("back_index", Number.class).intValue()), true);
    }

    public Slot buildBackSlot(int index){
        return new SimpleFixedSlot(this, index){
            @Override
            public void onClick(@NotNull HumanEntity player, @NotNull InventoryClickEvent event) {
                if(!(player instanceof Player p)) return;

                ShopTraderMenu backMenu = info().getOrThrow(ShopTraderMenu.class);
                backMenu.reload();
                backMenu.open(p);

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
