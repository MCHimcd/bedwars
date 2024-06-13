package mc.bedwars;

import mc.bedwars.Command.test;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static mc.bedwars.game.GameState.reset;

public final class BedWars extends JavaPlugin implements Listener {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        try {
            Objects.requireNonNull(Bukkit.getPluginCommand("test")).setExecutor(new test());
        }catch (NullPointerException e) {
            getLogger().warning(e.getMessage());
        }
        getServer().getPluginManager().registerEvents(this, this);
        reset();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var p = event.getPlayer();
        p.setGameMode(GameMode.SPECTATOR);

    }
}