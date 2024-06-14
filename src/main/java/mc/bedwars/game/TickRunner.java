package mc.bedwars.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import static mc.bedwars.game.GameState.*;

public class TickRunner extends BukkitRunnable {
    @Override
    public void run() {
        if (started) {
            players_data.forEach((_, playerData) -> {
                if (playerData.getAction() <= 0 && playerData.getOrder() == order) {
                    nextPlayer();
                }
            });

        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            var r = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 100, 10, entity -> entity.getType() == EntityType.MARKER);
            var pd = players_data.get(player);
            if (r != null) {
                var e = r.getHitEntity();
                if (e != null) {
                    //noinspection SuspiciousMethodCalls
                    pd.target_location = map.markers.get(e);
                }
            } else pd.target_location = null;
        });
    }
}