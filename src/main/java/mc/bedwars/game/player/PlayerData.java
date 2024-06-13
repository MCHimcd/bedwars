package mc.bedwars.game.player;

import mc.bedwars.BedWars;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mc.bedwars.game.GameState.end;
import static mc.bedwars.game.GameState.players_data;

public class PlayerData {
    private static int order_g = 1;
    private final Player player;
    //最大血量
    private final int MaxHealth = 100;
    //可使用的卡牌
    public List<Card> items = new ArrayList<>();
    //不可使用的卡牌
    public List<Card> equipments = new ArrayList<>();
    public Node location = null;
    private int order = 0;
    //经济
    private int money = 8;
    //是否拥有床
    private boolean has_bed = true;
    //行动力
    private int action = 1;
    //当前血量
    private int health = 100;
    //初始战力
    private int power = 2;
    private int max_power = getMaxPower();
    //临时战力
    private int Dpower = 0;

    public PlayerData(Player player) {
        this.player = player;
        order = order_g++;
        if (order_g == 5) order_g = 0;
        players_data.put(player, this);
    }

    public static void pvp(List<Player> players) {
        var p1 = new ArrayList<Player>();
        var p2 = new ArrayList<Player>();
        //选择队伍 todo
        //计算战力
        int power1 = power(p1);
        int power2 = power(p2);
        while (power1 == power2) {
            power1 = power(p1);
            power2 = power(p2);
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
                players_data.get(winners.getFirst()).addMoney(finalMoney(total_money));

            } else if (total_money <= 32) {
                PlayerData w = players_data.get(winners.getFirst());
                w.addMoney(16);
                if (final_dead) {
                    w.addMoney(finalMoney(total_money - 16));
                } else {
                    ld.addMoney(finalMoney(total_money - 16));
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
    }

    public static int finalMoney(int money) {
        return money == 0 ? 0 : Math.max(money, 8);
    }

    public static int power(List<Player> players) {
        var r = new Random();
        return players.stream().mapToInt(p -> players_data.get(p).getPower() + r.nextInt(1, 7)).sum();
    }

    public void hurt(int amount) {
        health = Math.max(health - amount, 1);
    }

    public void addMoney(int amount) {
        money = Math.min(money + amount, 64);
    }

    public void addDpower(int amount) {
        Dpower += amount;
    }
    public void addAction(int amount){
        action += amount;
    }

    public void resetDpower() {
        Dpower = 0;
    }
    public void resetAction() {
        action = 1;
    }

    public int getPower() {
        return getMaxPower() * ((health + 1) / 100) + Dpower;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int amount) {
        health = amount;
    }

    public int getMaxPower() {
        return power + items.stream().mapToInt(Card::power).sum() + equipments.stream().mapToInt(Card::power).sum();
    }

    public boolean removeMoney(int amount) {
        if (money < amount) {
            return false;
        }
        money -= amount;
        return true;
    }

    public void destroyBed() {
        has_bed = false;
    }

    public int die(List<Player> killers) {
        var m = money;
        money = 0;
        items.removeIf(Card::CanDrop);
        equipments.removeIf(Card::CanDrop);
        //最终击杀
        if (!has_bed) {
            players_data.remove(player);
        }
        //检测结束
        if (players_data.size() == 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    end();
                }
            }.runTaskLater(BedWars.instance, 1);
        }
        return m;
    }

    public int getOrder() {
        return order;
    }
}