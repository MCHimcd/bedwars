package mc.bedwars.game;

import mc.bedwars.BedWars;
import mc.bedwars.factory.Message;
import mc.bedwars.game.card.boost.HealingSpring;
import mc.bedwars.game.card.props.EnderPearl;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.island.resource.Bed;
import mc.bedwars.game.map.node.island.resource.Resource;
import mc.bedwars.menu.JoinPVPMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static mc.bedwars.BedWars.*;

public final class GameState {
    public static boolean started = false;
    public static int turn;
    public static Map<Player, PlayerData> players_data = new Hashtable<>();
    public static GameMap map = null;
    public static int order = 1;

    public static void reset() {
        GameState.turn=0;
        //重置
        players_data.values().forEach(p -> p.getMarker().remove());
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            player.teleport(new Location(player.getWorld(), 0, 66, 0));
            player.sendMessage(Component.text("               §c§l 游戏已重置!"));
            player.getInventory().clear();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000,5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,100000,5));
        });
        players_data.clear();
        order = 0;
        if (map != null) map.markers.keySet().forEach(Entity::remove);
        map = new GameMap(Bukkit.getWorld("world"));
        red.removeEntries(red.getEntries());
        green.removeEntries(green.getEntries());
        blue.removeEntries(blue.getEntries());
        yellow.removeEntries(yellow.getEntries());
        started = false;
    }

    public static void start(List<Player> players) {
        if (players.size() != 4) {
            Bukkit.broadcast(Component.text("            §c游戏需要4名玩家参与！"));
            return;
        }
        new BukkitRunnable() {
            int t = 0;
            int a = 0;
            @Override
            public void run() {
                t++;
                if (t % 20 == 0) {
                    a++;
                    players.forEach(player -> player.playSound(player, Sound.UI_BUTTON_CLICK, .5f, 1f));
                    players.forEach(player -> {
                        player.showTitle(Title.title(Message.rMsg("<rainbow> --游戏加载中--"),
                                Message.rMsg("<gold>" + "■".repeat(a) + "<white>" + "□".repeat(5 - a)),
                                Title.Times.times(Duration.ofMillis(1000), Duration.ZERO, Duration.ZERO)));
                    });
                }
                if (t >= 100) {
                    cancel();
                    //开始

                    players.forEach(player -> {
                        new PlayerData(player);
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, .5f);
                        player.teleport(new Location(player.getWorld(), 0, 22, 0));
                        player.showBossBar(bossbar);
                    });
                    started = true;
                    nextTurn();
                }
            }
        }.runTaskTimer(instance, 0, 1);
    }

    public static void end() {
        //结束
        players_data.keySet().stream().findFirst().ifPresent(winner -> {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                player.sendMessage(Message.rMsg("      <gold><bold> %s <white>获得了最终的胜利".formatted(winner.getName())));
                winner.playSound(winner,Sound.ENTITY_FIREWORK_ROCKET_BLAST,1f,1f);
            });
        });
        reset();
    }

    public static void nextPlayer() {
        if (++order == 5) {
            nextTurn();
            return;
        }
        players_data.forEach((p, d) -> {
            p.getInventory().clear();
            if (d.getOrder() == order) {
                p.getServer().getOnlinePlayers().forEach(player -> {
                    player.sendMessage(Message.rMsg("               <gold>现在轮到<aqua><bold> %s <reset><gold>的回合了".formatted(p.getName())));
                });
                bossbar.name(Message.rMsg("<aqua><bold>%s</bold>回合<gold>  当前轮次:</gold><blue><bold>%s".formatted(p.getName(),turn)));
                p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,2f);
                var is = d.getActions();
                for (int i = 0; i < is.size(); i++) {
                    p.getInventory().setItem(i, is.get(i));
                }
            }
            if (d.location instanceof Resource r) {
                r.giveMoney(p);
            }
            for (var i :map.islands){
                if(i instanceof Resource r) {
                    map.markers.forEach((m,n)->{
                        if(r.equals(n)){
                            m.text(Component.text("%s岛".formatted(i.getType())).append(Component.text("\n%s存有数量:%d".formatted(i.getType().substring(2),r.getAmount()), NamedTextColor.RED)));
                        }
                    });
                }
            }
        });
    }

    public static void nextTurn() {
        order = 0;
        turn++;
        map.islands.forEach(node -> {
            if (node instanceof Resource r) r.generate();
        });
        players_data.forEach((it, playerData) -> {
            //重置临时战力
            playerData.resetDpower();
            //血量低于50回血
            if (playerData.getHealth() < 50) playerData.setHealth(playerData.getHealth() + 10);
            //重置行动力
            playerData.resetAction();
            //复活
            if (playerData.getNeedSpawn()) {
                playerData.spawn();
            }
            //重置目标
            playerData.resetTarget();
            var Hs = playerData.items.stream().filter(item -> item instanceof HealingSpring).findFirst();
            //生命泉水
            if (Hs.isPresent()) {
                if (playerData.location instanceof Bed bed && bed.getOrder() == playerData.getOrder()) {
                    playerData.addDpower(3);
                }
            }
        });
        nextPlayer();
    }

    public static void pvp(List<Player> players, Player attacker, Player target) {
        var p1 = new ArrayList<Player>();
        p1.add(attacker);
        var p2 = new ArrayList<Player>();
        p2.add(target);
        //选择队伍
        new BukkitRunnable() {
            private int t = 0;

            @Override
            public void run() {
                if (t++ >= 300) {
                    //计算战力
                    int power1 = PlayerData.power(p1);
                    int power2 = PlayerData.power(p2);
                    while (power1 == power2) {
                        power1 = PlayerData.power(p1);
                        power2 = PlayerData.power(p2);
                    }
                    List<Player> winners = power1 > power2 ? p1 : p2;
                    List<Player> losers = power1 > power2 ? p2 : p1;
            /*
            winners扣血机制:
            1.若胜利者(以下简称为W）队伍与失败者（以下简称为L) 队伍都只有一人;  则 W收到的伤害为L的战力值
            2.若W队伍中有多人，L中只有一人 则W中两人平分 等同于L的战力值的伤害
            3.若W队伍中只有一人 L中有多人， 则W受到的伤害值 为 L中个人战力x1/n (n为队伍人数) 然后求和
            4.若多对多  则先求L中个人战力x1/n (n为队伍人数)的和   然后胜利者队伍的玩家均摊;
            */
                    int hurt_amount = losers.stream().mapToInt(loser -> players_data.get(loser).getPower()).sum() / losers.size() / winners.size();
                    winners.forEach(winner -> players_data.get(winner).hurt(hurt_amount));
                    //分钱
                    losers.forEach(loser -> {
                        var total_money = players_data.get(loser).die(winners);
                        var ld = players_data.get(loser);
                        var final_dead = ld == null;
                        if (total_money <= 16) {
                            players_data.get(winners.getFirst()).addMoney(PlayerData.finalMoney(total_money));

                        } else if (total_money <= 32) {
                            PlayerData w = players_data.get(winners.getFirst());
                            w.addMoney(16);
                            if (final_dead) {
                                w.addMoney(PlayerData.finalMoney(total_money - 16));
                            } else {
                                ld.addMoney(PlayerData.finalMoney(total_money - 16));
                            }
                        } else {
                            //大于32
                            PlayerData w = players_data.get(winners.removeFirst());
                            w.addMoney(16);
                            if (final_dead) {
                                w.addMoney(16);
                            } else {
                                ld.addMoney(16);
                            }
                            total_money -= 32;
                            for (Player winner : winners) {
                                if (total_money > 0) {
                                    total_money = Math.max(total_money - 8, 0);
                                    players_data.get(winner).addMoney(8);
                                } else break;
                            }
                        }
                    });
                    players_data.get(attacker).addAction(-1);
                    cancel();
                }
            }
        }.runTaskTimer(BedWars.instance, 0, 1);
        players.stream().filter(p -> !p.equals(attacker) && !p.equals(target)).forEach(player -> player.openInventory(new JoinPVPMenu(player, p1, p2).getInventory()));
    }
}