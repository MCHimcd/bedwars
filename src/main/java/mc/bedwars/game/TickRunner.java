package mc.bedwars.game;

import mc.bedwars.BedWars;
import mc.bedwars.factory.Message;
import mc.bedwars.factory.particle;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.menu.MainMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static mc.bedwars.game.GameState.*;
import static mc.bedwars.menu.MainMenu.prepared;
import static mc.bedwars.menu.MainMenu.start_tick;
import static mc.bedwars.menu.SkinMenu.itemNames;
import static mc.bedwars.menu.SkinMenu.skins;

public class TickRunner extends BukkitRunnable {
    public static boolean ending = false;
    public static TextDisplay himcd;
    private int himcd_timer = 0;

    @Override
    public void run() {
        if (started) {
            players_data.forEach((player, pd) -> {
                //下一个玩家
                if (pd.getAction() <= 0 && pd.getOrder() == order && !ending) {
                    nextPlayer();
                }
                //传送
                if (player.isSneaking() && pd.canTp) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1, 255, false, false));
                    if (pd.snakeTime % 5 == 0) {
                        graph(player).forEach(location ->
                                player.spawnParticle(Particle.DUST_COLOR_TRANSITION, location, 1, 0, 0, 0, 0, new Particle.DustTransition(Color.BLUE, Color.AQUA, 1f))
                        );
                        player.showTitle(Title.title(Message.rMsg("<rainbow> --传送中--"),
                                Message.rMsg("<gold>" + "■".repeat(pd.snakeTime / 5) + "<white>" + "□".repeat(4 - pd.snakeTime / 5)),
                                Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ZERO)));
                    }
                    if (pd.snakeTime++ >= 20) {
                        var l = pd.getMarker().getLocation();
                        l.setY(player.getY());
                        player.teleport(l);
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, .5f);
                        player.clearTitle();
                        pd.canTp = false;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                pd.canTp = true;
                            }
                        }.runTaskLater(BedWars.instance, 40);
                        pd.snakeTime = 0;
                    }
                } else if (pd.snakeTime > 0) {
                    player.showTitle(Title.title(Message.rMsg("<rainbow> --已终止传送--"), Component.empty(), Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ZERO)));
                    pd.snakeTime = 0;
                }
            });
        } else {
            //大厅信息显示
            Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(
                    Message.rMsg("<color:#FFF2E2>你选择的皮肤是：")
                            .append(itemNames.get(skins.getOrDefault(player, 90000) - 90000))
                            .append(Message.rMsg(" <color:#EAEAEF>| <color:#DCE2F1>你将会在<gold>%s层<color:#DCE2F1>下棋".formatted(MainMenu.up.getOrDefault(player, true) ? "上" : "下")))
            ));
            //开始倒计时
            if (start_tick >= 0 && start_tick < 200) {
                if (start_tick % 20 == 0) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.showTitle(Message.title("%d".formatted((200 - start_tick) / 20), "", 0, 1000, 0));
                        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    });
                }
                start_tick++;
                if (start_tick == 200) {
                    start(prepared);
                }
            }
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!started) return;
            var pd = players_data.get(player);
            if (pd == null) return;
            player.sendActionBar(Message.msg.deserialize("|<name>|", Placeholder.component("name", player.teamDisplayName()))
                    .append(pd.canPVP() ? Component.text(" §dPVP目标:").append(pd.getTarget() == null ? Component.text("无", NamedTextColor.DARK_GRAY) : pd.getTarget().teamDisplayName()) : Component.empty())
                    .append(
                            pd.target_location != null ?
                                    Message.rMsg(" <color:#B7E8BD>目标岛屿:").append(Component.text(pd.target_location.toString())) :
                                    Component.empty()
                    )
                    .append(Component.text(" §b战力值:%s/%s§c 血量值:%s §6经济:%s/64 §9行动力:%s".formatted(
                            pd.getPower(),
                            pd.getMaxPower(),
                            pd.getHealth(),
                            pd.getMoney(),
                            pd.getAction()))));
            var r = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection(), 100, 5, entity -> entity.getType() == EntityType.TEXT_DISPLAY);
            if (r != null) {
                var e = r.getHitEntity();
                if (e != null) {
                    //noinspection SuspiciousMethodCalls
                    pd.target_location = (Island) map.markers.get(e);
                }
            } else pd.target_location = null;
        });

        if (himcd != null) {
            if (himcd_timer++ == 20) himcd_timer = 0;
            himcd.text(Message.rMsg("<rainbow:%d>极熠-Himcd".formatted(himcd_timer)));
        }
    }

    public List<Location> graph(Player player) {
        List<Location> locations = new ArrayList<>();
        for (double a = 0; a <= 6.29; a += 0.1) {
            double x = 1 * Math.cos(a);
            double y = a / 3;
            double z = 1 * Math.sin(a);
            Location loc = particle.roloc(player, x, y, z, 0);
            locations.add(loc);
        }
        return locations;
    }
}