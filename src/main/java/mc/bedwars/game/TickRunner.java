package mc.bedwars.game;

import mc.bedwars.game.player.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import static mc.bedwars.game.GameState.*;

public class TickRunner extends BukkitRunnable {
    @Override
    public void run() {
        if (started) {
            players_data.forEach((player, playerData) -> {
                if (playerData.getAction()<=0&&playerData.getOrder()==order){
                    nextPlayer();
                }
            });

        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            var r = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 100, 10, entity -> entity.getType() == org.bukkit.entity.EntityType.ARMOR_STAND);
            if (r != null) {
                var e = r.getHitEntity();
                if (e != null) {
                    e.getScoreboardTags().forEach(t -> {
                        if(t.startsWith("i")) {
                            var p=t.split(" ");
                            var x=Integer.parseInt(p[1]);
                            var y=Integer.parseInt(p[2]);
                            var pd=players_data.get(player);

                        }
                    });
                }
            }
        });
    }
}