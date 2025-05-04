package killercreepr.cruxshops.core;

import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.communication.lang.LangProvider;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.communication.lang.LangPopulator;
import killercreepr.crux.core.communication.lang.Msg;
import killercreepr.crux.core.communication.lang.SimpleCreateLang;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.standard.SimpleLangConfig;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxcore.CruxCore;
import killercreepr.cruxshops.api.profession.TraderProfession;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeIngredient;
import killercreepr.cruxshops.api.shop.trade.ShopTradeResult;
import killercreepr.cruxshops.api.shop.trade.TraderTrade;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.config.Config;
import killercreepr.cruxshops.core.lang.Lang;
import killercreepr.cruxshops.core.menu.ShopTraderMenu;
import killercreepr.cruxshops.core.shop.trade.ingredient.PluginItemTradeIngredient;
import killercreepr.cruxshops.core.shop.trade.result.PluginItemTradeResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CruxShops extends CruxPlugin implements Listener, LangProvider {
    private static CruxShops instance;
    public static CruxShops inst(){ return instance; }

    protected Config values;

    public Config values() {
        return values;
    }

    public void values(@NotNull Config values) {
        this.values = values;
    }

    protected final CreateLang lang = Lang.setLang(new SimpleCreateLang());
    @NotNull
    public CreateLang lang() {
        return lang;
    }

    protected LangProvider langProvider;

    @Override
    public void onLoad() {
        instance = this;
        super.onLoad();
    }

    @Override
    public void enabled() {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            values(new Config(this, "config"));
        }else{
            //values(new DefaultValues());
        }

        registerListeners(
            this
        );

        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            langProvider = new SimpleLangConfig(this, "lang", this::lang, Lang.class);
        }else{
            langProvider = this;
            LangPopulator.populate(lang, Msg.class);
        }

        super.enabled();
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        var menuHolder = CruxCore.inst().cruxMenus().menuRegistry().menuHolders()
            .get(Crux.key("testboy"));

        TraderTrade trader = new TraderTrade() {
            @Nullable
            @Override
            public ShopTrade getBuyingTrade() {
                return new ShopTrade() {
                    @NotNull
                    @Override
                    public List<ShopTradeIngredient> getIngredients() {
                        return List.of(
                            new PluginItemTradeIngredient(
                                Crux.handlers().item().getItem(Crux.key("crux:orbit")), 3
                            )
                        );
                    }

                    @NotNull
                    @Override
                    public List<ShopTradeResult> getResults() {
                        return List.of(
                            new PluginItemTradeResult(
                                Crux.handlers().item().getItem(Crux.key("minecraft:iron_ingot")), 1
                            )
                        );
                    }

                    @Override
                    public boolean canAfford(@NotNull Entity p) {
                        return true;
                    }

                    @Override
                    public boolean canAccept(@NotNull Entity p) {
                        return true;
                    }

                    @Override
                    public boolean canView(@NotNull Entity p) {
                        return true;
                    }

                    @Override
                    public void purchase(@NotNull Entity p) {

                    }
                };
            }

            @Nullable
            @Override
            public ShopTrade getSellingTrade() {
                return new ShopTrade() {
                    @NotNull
                    @Override
                    public List<ShopTradeIngredient> getIngredients() {
                        return List.of(
                            new PluginItemTradeIngredient(
                                Crux.handlers().item().getItem(Crux.key("minecraft:iron_ingot")), 1
                            )
                        );
                    }

                    @NotNull
                    @Override
                    public List<ShopTradeResult> getResults() {
                        return List.of(
                            new PluginItemTradeResult(
                                Crux.handlers().item().getItem(Crux.key("crux:orbit")), 1
                            )
                        );
                    }

                    @Override
                    public boolean canAfford(@NotNull Entity p) {
                        return true;
                    }

                    @Override
                    public boolean canAccept(@NotNull Entity p) {
                        return true;
                    }

                    @Override
                    public boolean canView(@NotNull Entity p) {
                        return true;
                    }

                    @Override
                    public void purchase(@NotNull Entity p) {

                    }
                };
            }
        };

        ShopTrader shopTrader = new ShopTrader() {
            @NotNull
            @Override
            public TraderProfession getProfession() {
                return null;
            }

            @NotNull
            @Override
            public List<TraderTrade> getTrades(@NotNull Entity viewer) {
                return List.of(
                    trader,trader,trader,trader,trader,trader,trader,trader,trader,trader,trader,
                    trader
                );
            }
        };

        ShopTraderMenu menu = new ShopTraderMenu(
            menuHolder, DataExchange.builder()
            .put("viewer", p)
            .put("trader", shopTrader)
            .build()
        );
        menu.load();
        menu.open(p);
    }


    @Override
    public void disabled() {
        super.disabled();
    }

    @Override
    public void reload() {
        super.reload();
        values.reload();
        langProvider.reload(this);

        CruxCore.inst().cruxMenus().menuRegistry().loadConfiguration(
            new CruxFolder(this, "menus").file()
        );
    }

    public void registerCfgHandlers(@NotNull FileRegistry registry){
    }
}
