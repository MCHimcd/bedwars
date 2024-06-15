package mc.bedwars.game;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Marker;
import org.bukkit.scheduler.BukkitRunnable;

import static mc.bedwars.game.GameState.*;

public class TickRunner extends BukkitRunnable {
    @Override
    public void run() {
        if (started) {
            players_data.forEach((player, playerData) -> {
                if (playerData.getAction() <= 0 && playerData.getOrder() == order) {
                    nextPlayer();
                }
            });
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(!started) return;
            String target;
            var pd = players_data.get(player);
            if (pd.getTarget()==null){
                target=null;
            }else target=pd.getTarget().getName();
            player.sendActionBar(Component.text("§6|%s|§d当前目标%s§a当前目标岛屿：%s §b当前战力值：%s/%s§c当前血量值: %s §6当前经济:%s/64 §9当前行动力:%s".formatted(
                    player.getName(),
                    target,
                    pd.target_location,
                    pd.getPower(),
                    pd.getMaxPower(),
                    pd.getHealth(),
                    pd.getMoney(),
                    pd.getAction())));
            var r = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 100, 5, entity -> entity.getType() == EntityType.TEXT_DISPLAY);
            if (r != null) {
                var e = r.getHitEntity();
                if (e != null) {
                    pd.target_location =map.markers.get(e);
                }
            } else pd.target_location = null;
        });
    }
}