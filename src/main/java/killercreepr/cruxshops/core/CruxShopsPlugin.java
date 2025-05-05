package killercreepr.cruxshops.core;

import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.communication.lang.LangProvider;
import killercreepr.crux.core.communication.lang.LangPopulator;
import killercreepr.crux.core.communication.lang.Msg;
import killercreepr.crux.core.communication.lang.SimpleCreateLang;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.standard.SimpleLangConfig;
import killercreepr.cruxcore.CruxCore;
import killercreepr.cruxshops.core.command.CruxShopCommands;
import killercreepr.cruxshops.core.component.CruxShopsComponents;
import killercreepr.cruxshops.core.config.CfgHook;
import killercreepr.cruxshops.core.config.Config;
import killercreepr.cruxshops.core.lang.Lang;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public void onLoad() {
        instance = this;
        super.onLoad();
        CruxShopsComponents.register();
        CfgHook.load();

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

    @Override
    public void disabled() {
        super.disabled();
    }

    @Override
    public void reload() {
        super.reload();
        values.reload();
        langProvider.reload(this);

        CfgHook.reload(this);

        CruxCore.inst().cruxMenus().menuRegistry().loadConfiguration(
            new CruxFolder(this, "menus").file()
        );
    }
}
