package mc.bedwars.game;

import mc.bedwars.BedWars;
import mc.bedwars.factory.ItemCreator;
import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.game.map.node.island.resource.Bed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mc.bedwars.game.GameState.*;
import static mc.bedwars.BedWars.*;

public class PlayerData {
    //目标
    static public Player target = null;
    private static int order_g = 1;
    private final Player player;
    //最大血量
    private final int MaxHealth = 100;
    public double distanceX = 0;
    public double distanceZ = 0;
    //可使用的卡牌
    public List<Card> items = new ArrayList<>();
    //不可使用的卡牌
    public List<Card> equipments = new ArrayList<>();

    public Node location = null;
    public Node target_location = null;
    //当前选择的保护床方块层数
    private int protectBed = 1;
    //左1层床保护方块
    private Material LfirstBedBlock = Material.AIR;
    //左2层床保护方块
    private Material LsecondBedBlock = Material.WHITE_WOOL;
    //左3层床保护方块
    private Material LthirdBedBlock = Material.WHITE_WOOL;

    //右1层床保护方块
    private Material RfirstBedBlock = Material.AIR;
    //右2层床保护方块
    private Material RsecondBedBlock = Material.WHITE_WOOL;
    //右3层床保护方块
    private Material RthirdBedBlock = Material.WHITE_WOOL;
    //将要破坏的层数
    private int destroyBedBlock = 1;
    private ArmorStand marker;
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
    private boolean needSpawn = false;

    public PlayerData(Player player) {
        this.player = player;
        order = order_g++;
        Team team = switch (order) {
            case 1 -> red;
            case 2 -> green;
            case 3 -> blue;
            case 4 -> yellow;
            default -> null;
        };
        team.addPlayer(player);
        map.islands.stream().filter(node -> {
            if (node instanceof Bed bed) {
                return bed.getOrder() == order;
            }
            return false;
        }).findFirst().ifPresent(bed -> {
            location = bed;
            marker = player.getWorld().spawn(GameMap.getLocation(bed), ArmorStand.class, ar -> {
                team.addEntity(ar);
                ar.setMarker(true);
                ar.setGlowing(true);
                ar.getEquipment().setHelmet(new ItemStack(Material.PLAYER_HEAD) {{
                    editMeta(m -> {
                        if (m instanceof SkullMeta meta) meta.setOwningPlayer(player);
                    });
                }});
            });
        });
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

    public int getMoney() {
        return money;
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

    public int getDestroyBedBlock() {
        return destroyBedBlock;
    }

    public void setDestroyBedBlock(int count) {
        destroyBedBlock = count;
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

    public int getProtectBed() {
        return protectBed;
    }

    public void setProtectBed(int amount) {
        protectBed = amount;
    }

    public Material protectBedBlockMaterial(int amount) {
        if (amount == 0) {
            return LfirstBedBlock;
        }
        if (amount == 1) {
            return LsecondBedBlock;
        }
        if (amount == 2) {
            return LthirdBedBlock;
        }
        if (amount == 3) {
            return RthirdBedBlock;
        }
        if (amount == 4) {
            return RsecondBedBlock;
        }
        if (amount == 5) {
            return RfirstBedBlock;
        }
        return Material.AIR;
    }

    public void placeBedBlock(Material material) {
        if (protectBed == 1) {
            LfirstBedBlock = material;
        }
        if (protectBed == 2) {
            LsecondBedBlock = material;
        }
        if (protectBed == 3) {
            LthirdBedBlock = material;
        }
        if (protectBed == 4) {
            RthirdBedBlock = material;
        }
        if (protectBed == 5) {
            RsecondBedBlock = material;
        }
        if (protectBed == 6) {
            RfirstBedBlock = material;
        }
    }

    public int getPower() {
        return getMaxPower() * (health / 100) + Dpower;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int amount) {
        health = amount;
    }

    public boolean getNeedSpawn() {
        return needSpawn;
    }

    public void spawn() {
        map.islands.stream().filter(i -> {
            return i instanceof Bed b && b.getOrder() == order;
        }).findFirst().ifPresent(island -> marker.teleport(GameMap.getLocation(island)));
        needSpawn = false;
        action = 0;
    }

    public int getMaxPower() {
        return power + items.stream().mapToInt(Card::power).sum() + equipments.stream().mapToInt(Card::power).sum();
    }

    public int getAction() {
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
        Bukkit.getServer().getOnlinePlayers().forEach(player1 -> {
            player1.sendMessage(Message.rMsg("       <red>%s<red> <bold>的床被破坏!".formatted(player.getName())));
        });
    }

    public int die(List<Player> killers) {
        var m = money;
        money = 0;
        items.removeIf(Card::CanDrop);
        equipments.removeIf(Card::CanDrop);
        //最终击杀
        if (!has_bed) {
            players_data.remove(player);
            Bukkit.getServer().getOnlinePlayers().forEach(player1 -> {
                player1.sendMessage(Message.rMsg("          <red>%s</red> <bold>被最终淘汰!".formatted(player.getName())));
            });
            player.teleport(new Location(player.getWorld(),0,22,0));
            player.showTitle(Title.title(Message.rMsg("<aqua>观战"),Message.rMsg("<rainbow>--------")));
        } else {
            needSpawn = true;
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
        //移动 60007
        items.add(ItemCreator.create(Material.PAPER).data(60007).name(Component.text("移动")).getItem());
        //搭路 60003
        if (location instanceof Island) {
            items.add(ItemCreator.create(Material.PAPER).data(60003).name(Component.text("搭路")).getItem());
        }
        //pvp 60002
        if (location instanceof Island i) {
            if (i.players.size() > 1) {
                items.add(ItemCreator.create(Material.PAPER).data(60002).name(Component.text("PVP")).getItem());
            }
        }
        //使用道具 60008
        items.add(ItemCreator.create(Material.PAPER).data(60008).name(Component.text("使用道具")).getItem());
        //破坏60006
        items.add(ItemCreator.create(Material.PAPER).data(60006).name(Component.text("破坏桥")).getItem());
        //商店60005、床60004  他人60010
        if (location instanceof Bed b) {
            //商店
            items.add(ItemCreator.create(Material.PAPER).data(60005).name(Component.text("商店")).getItem());
            if (b.getOrder() == order) {
                //自己床
                items.add(ItemCreator.create(Material.PAPER).data(60004).name(Component.text("守护床")).getItem());
            } else {
                //别人床
                items.add(ItemCreator.create(Material.PAPER).data(60010).name(Component.text("破坏该床")).getItem());
            }
        }
        //选择目标 60009
        if (location.players.size() > 2) {
            items.add(ItemCreator.create(Material.PAPER).data(60009).name(Component.text("选择目标")).getItem());
        }
        //跳过回合
        items.add(ItemCreator.create(Material.PAPER).data(60011).name(Component.text("跳过回合")).getItem());

        return items;
    }

    public ArmorStand getMarker() {
        return marker;
    }
}