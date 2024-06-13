package mc.bedwars.game.player;

import mc.bedwars.BedWars;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.game.map.node.island.resource.Bed;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mc.bedwars.game.GameState.*;

public class PlayerData {
    //目标
    static public Player target = null;
    private static int order_g = 1;
    private final Player player;
    //最大血量
    private final int MaxHealth = 100;
    //可使用的卡牌
    public List<Card> items = new ArrayList<>();
    //不可使用的卡牌
    public List<Card> equipments = new ArrayList<>();
    public Node location = null;
    public Node target_location = null;
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
    private boolean needSpawn=false;

    public PlayerData(Player player) {
        this.player = player;
        order = order_g++;
        map.nodes.stream().filter(node -> {
            if (node instanceof Bed bed) {
                return bed.getOrder() == order;
            }
            return false;
        }).findFirst().ifPresent(bed -> location = bed);
        if (order_g == 5) order_g = 1;
        players_data.put(player, this);
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

    public void addAction(int amount) {
        action += amount;
    }

    public void resetDpower() {
        Dpower = 0;
    }

    public void resetAction() {
        action = 1;
    }

    public void resetTarget() {
        target = null;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player player) {
        target = player;
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
    public boolean getNeedSpawn(){
        return needSpawn;
    }

    public int getMaxPower() {
        return power + items.stream().mapToInt(Card::power).sum() + equipments.stream().mapToInt(Card::power).sum();
    }
    public int getAction(){
        return action;
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
        }else {
            needSpawn=true;
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

    public List<ItemStack> getActions() {
        List<ItemStack> items = new ArrayList<>();
        //移动
        items.add(new ItemStack(Material.PAPER) {{
            editMeta(m -> m.setCustomModelData(60007));
        }});
        //搭路
        items.add(new ItemStack(Material.PAPER) {{
            editMeta(m -> m.setCustomModelData(60003));
        }});
        //pvp
        if (location instanceof Island i) {
            if (i.players.size() > 1) {
                items.add(new ItemStack(Material.PAPER) {{
                    editMeta(m -> m.setCustomModelData(60002));
                }});
            }
        }
        //使用道具
        items.add(new ItemStack(Material.PAPER) {{
            editMeta(m -> m.setCustomModelData(60008));
        }});
        //破坏
        items.add(new ItemStack(Material.PAPER) {{
            editMeta(m -> m.setCustomModelData(60006));
        }});
        //商店、床
        if (location instanceof Bed b) {
            //商店
            items.add(new ItemStack(Material.PAPER) {{
                editMeta(m -> m.setCustomModelData(60005));
            }});
            if (b.getOrder() == order) {
                //自己床
                items.add(new ItemStack(Material.PAPER) {{
                    editMeta(m -> m.setCustomModelData(60004));
                }});
            } else {
                //别人床
                items.add(new ItemStack(Material.PAPER) {{
                    editMeta(m -> m.setCustomModelData(60004));
                }});
            }
        }
        //选择目标
        if (location.players.size() > 2) {
            items.add(new ItemStack(Material.PAPER) {{
                editMeta(m -> m.setCustomModelData(60009));
            }});
        }

        return items;
    }
}