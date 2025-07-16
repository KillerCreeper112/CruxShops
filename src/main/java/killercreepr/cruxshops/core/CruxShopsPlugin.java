package killercreepr.cruxshops.core;

import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.communication.lang.LangProvider;
import killercreepr.crux.api.data.Loadable;
import killercreepr.crux.api.data.PluginLoadable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.communication.lang.LangPopulator;
import killercreepr.crux.core.communication.lang.Msg;
import killercreepr.crux.core.communication.lang.SimpleCreateLang;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.file.BukkitDataFile;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.standard.SimpleLangConfig;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxcore.CruxCore;
import killercreepr.cruxshops.api.market.Market;
import killercreepr.cruxshops.core.command.CruxShopCommands;
import killercreepr.cruxshops.core.component.CacheTraderTradeDemandComponent;
import killercreepr.cruxshops.core.component.CruxShopsComponents;
import killercreepr.cruxshops.core.config.CfgHook;
import killercreepr.cruxshops.core.config.Config;
import killercreepr.cruxshops.core.lang.Lang;
import killercreepr.cruxshops.core.listener.CustomObjectiveListener;
import killercreepr.cruxshops.core.market.SimpleMarket;
import killercreepr.cruxshops.core.menu.ShopTraderMenu;
import killercreepr.cruxshops.core.registries.ShopsRegistries;
import killercreepr.cruxshops.core.text.tags.object.ShopTradeTags;
import killercreepr.cruxshops.core.text.tags.object.ShopTraderTags;
import killercreepr.cruxshops.core.text.tags.object.TraderTradeTags;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CruxShopsPlugin extends CruxPlugin implements Listener, LangProvider {
    private static CruxShopsPlugin instance;
    public static CruxShopsPlugin inst(){ return instance; }

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

    protected Market globalMarket;

    public Market getGlobalMarket() {
        return globalMarket;
    }

    public void setGlobalMarket(Market globalMarket) {
        this.globalMarket = globalMarket;
    }

    @Override
    public void onLoad() {
        instance = this;
        super.onLoad();
        CruxShopsComponents.register();
        CfgHook.load();

        Crux.tags().register(
            new ShopTradeTags(),
            new TraderTradeTags(),
            new ShopTraderTags()
        );

        new CruxShopCommands(this).register();
    }

    @Override
    public void enabled() {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            values(new Config(this, "config"));
        }else{
            //values(new DefaultValues());
        }

        registerListeners(
            this,
            new CustomObjectiveListener()
        );

        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            langProvider = new SimpleLangConfig(this, "lang", this::lang, Lang.class);
        }else{
            langProvider = this;
            LangPopulator.populate(lang, Msg.class);
        }

        super.enabled();

        if(!values.USURVIVE_SPAWN_LOGIC.valueOr(false)) return;
        new BukkitRunnable(){
            World world;
            @Override
            public void run() {
                if(world == null){
                    world = getServer().getWorld("world_spawn");
                    if(world == null) return;
                }

                if(Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)) && world.getTime() == 0){
                    getLogger().info("Updating cache in CruxShops");
                    for(var trader : ShopsRegistries.SHOP_TRADER){
                        trader.getProfession().getAllTrades().forEach(trade ->{
                            var data = trade.get(CruxShopsComponents.TRADER_TRADE_DEMAND);
                            if(!(data instanceof CacheTraderTradeDemandComponent c)) return;
                            data.setSupply(c.getCacheSupply());
                            data.setDemand(c.getCacheDemand());
                        });
                    }
                    ShopTraderMenu.updateMenus();
                }
            }
        }.runTaskTimer(this, 10L, 1L);
    }

    @Override
    public void disabled() {
        super.disabled();
        globalMarket.setStopped();

        ShopsRegistries.SHOP_TRADER.forEach(shop ->{
            if(shop instanceof PluginLoadable l){
                l.save(this);
            }else if(shop instanceof Loadable l){
                l.save();
            }
        });
    }

    @Override
    public void reload() {
        super.reload();
        values.setup();
        langProvider.reload(this);

        CfgHook.reload(this);

        CruxCore.inst().cruxMenus().menuRegistry().loadConfiguration(
            new CruxFolder(this, "menus").file()
        );

        if(globalMarket != null){
            globalMarket.setStopped();
            globalMarket = null;
        }
        DataFile file = BukkitDataFile.parseFromGeneralPath(
            new CruxFolder(this, "market").file(), false
        );
        if(file == null){
            return;
        }
        NumberProvider tick = file.deserialize("tick_delay", NumberProvider.class);
        file.close();
        globalMarket = new SimpleMarket(
            this, tick.value().intValue(), ShopsRegistries.SHOP_TRADER, NumberProvider.zero(), Map.of(),
            NumberProvider.zero(), NumberProvider.zero()
        );
        globalMarket.setStarted();
    }
}
