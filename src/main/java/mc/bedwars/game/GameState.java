package mc.bedwars.game;

import mc.bedwars.BedWars;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.resource.Resource;
import mc.bedwars.game.player.PlayerData;
import mc.bedwars.menu.JoinPVPMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public final class GameState {
    public static boolean started = false;
    public static Map<Player, PlayerData> players_data = new Hashtable<>();
    public static GameMap map;
    public static int order = 1;

    public static void reset() {
        //重置
        players_data.clear();
        order = 0;
        map = new GameMap();
        started = false;
    }

    public static void start(List<Player> players) {
        if (players.size() != 4) {
            Bukkit.broadcast(Component.text("§c游戏需要4名玩家参与！"));
            return;
        }
        //开始
        players.forEach(PlayerData::new);
        started = true;
        nextTurn();
        nextPlayer();
    }

    public static void end() {
        //结束
        players_data.keySet().stream().findFirst().ifPresent(winner -> winner.sendMessage(Component.text("§a你获得最终胜利！")));
        reset();
    };

    public static void nextPlayer() {
        if (++order == 5) {
            nextTurn();
        }
        players_data.forEach((p, d) -> {
            p.getInventory().clear();
            if (d.getOrder() == order) {
                var is = d.getActions();
                for (int i = 0; i < is.size(); i++) {
                    p.getInventory().setItem(i, is.get(i));
                }
            }
        });
    }

    public static void nextTurn() {
        order = 0;
        map.nodes.forEach(node -> {
            if (node instanceof Resource r) r.generate();
        });
        players_data.forEach((player, playerData) -> {
            //重置临时战力
            playerData.resetDpower();
            //血量低于50回血
            if (playerData.getHealth() < 50) playerData.setHealth(playerData.getHealth() + 10);
            //重置行动力
            playerData.resetAction();
            if (playerData.getNeedSpawn()){
                playerData.addAction(-1);
            }
            //重置目标
            playerData.resetTarget();
        });
    }

    public static void pvp(List<Player> players,Player attacker,Player target) {
        var p1 = new ArrayList<Player>();
        p1.add(attacker);
        var p2 = new ArrayList<Player>();
        p2.add(target);
        //选择队伍
        new BukkitRunnable() {
            private int t=0;
            @Override
            public void run() {
                if(t++>=300){
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
                    cancel();
                }
            }
        }.runTaskTimer(BedWars.instance, 0, 1);
        players.stream().filter(p -> !p.equals(attacker) && !p.equals(target)).forEach(player -> {
            player.openInventory(new JoinPVPMenu(player,p1,p2).getInventory());
        });
    }
}