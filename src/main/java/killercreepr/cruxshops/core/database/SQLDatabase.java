package killercreepr.cruxshops.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import killercreepr.cruxshops.api.database.ShopDatabase;
import killercreepr.cruxshops.api.shop.trade.ShopTrade;
import killercreepr.cruxshops.api.shop.trade.ShopTradeObject;
import killercreepr.cruxshops.api.trader.ShopTrader;
import killercreepr.cruxshops.core.shop.trade.CruxCurrencyTradeObject;
import killercreepr.cruxshops.core.shop.trade.PluginItemTradeObject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLDatabase implements ShopDatabase {
  protected final Plugin plugin;
  protected final String url;
  protected final String username;
  protected final String password;
  protected final HikariDataSource dataSource;

  public SQLDatabase(Plugin plugin, String url, String username, String password) {
    this.plugin = plugin;
    this.url = url;
    this.username = username;
    this.password = password;
    var cfg = new HikariConfig();
    cfg.setJdbcUrl(url);
    cfg.setUsername(username);
    cfg.setPassword(password);
    this.dataSource = new HikariDataSource(cfg);
    createTableIfNotExists();
  }

  protected void createTableIfNotExists(){
    try(Connection connection = dataSource.getConnection()){
      var statement = connection.prepareStatement("""
      CREATE TABLE IF NOT EXISTS npc_trade_stats (
                    npc_trader_key VARCHAR(64),
                    trade_key VARCHAR(64),
      
                    total_currency BIGINT NOT NULL DEFAULT 0,
      
                    use_count BIGINT NOT NULL DEFAULT 0,
      
                    PRIMARY KEY (npc_trader_key, trade_key)
                );
      """);
      statement.executeUpdate();
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  public @Nullable Key getTradeKey(ShopTradeObject object) {
    if (object instanceof CruxCurrencyTradeObject i) {
      return i.getCurrencyHolder().value().key();
    } else if (object instanceof PluginItemTradeObject i) {
      return i.getItemHolder().key();
    }
    return null;
  }

  public void onTradeUse(ShopTrader trader, ShopTrade trade) {
    if (!(trader instanceof Keyed traderKeyed)) return;

    //only track data from simple trades
    if (trade.getIngredients().size() != 1) return;
    if (trade.getResults().size() != 1) return;

    var result = trade.getResults().getFirst();
    var ingredient = trade.getIngredients().getFirst();

    var ingredientKey = getTradeKey(ingredient);
    var resultKey = getTradeKey(result);
    if (ingredientKey == null || resultKey == null) return;

    //trade is trading ingredient for currency
    boolean selling = result instanceof CruxCurrencyTradeObject;
    var currency = selling ? result.getAmount() : ingredient.getAmount();
    var tradeKey = ingredientKey.asString() + "_for_" + resultKey.asString();
    var traderKey = traderKeyed.key().asString();

    //todo batching eventually, to reduce database queries
    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
      try(Connection connection = dataSource.getConnection()){
        var statement =  connection.prepareStatement("""
          INSERT INTO npc_trade_stats (npc_trader_key, trade_key, total_currency, use_count)
          VALUES (?, ?, ?, 1)
          ON DUPLICATE KEY UPDATE
              total_currency = total_currency + VALUES(total_currency),
              use_count = use_count + 1;
          """);
        statement.setString(1, traderKey);
        statement.setString(2, tradeKey);
        statement.setInt(3, currency);
        statement.executeUpdate();
      }catch(SQLException e){
        e.printStackTrace();
      }
    });
  }

  public void close() {
    if(dataSource != null) dataSource.close();
  }
}
