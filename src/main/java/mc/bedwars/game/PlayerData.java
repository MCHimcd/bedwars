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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mc.bedwars.BedWars.*;
import static mc.bedwars.game.GameState.*;

public class PlayerData {
    //目标
    static public Player target = null;
    private static int order_g = 1;
    private final Player player;
    public int snakeTime = 0;
    public boolean canTp = true;
    //可使用的卡牌
    public List<Card> items = new ArrayList<>();
    //不可使用的卡牌
    public List<Card> equipments = new ArrayList<>();

    public Node location = null;
    public Island target_location = null;
    public Island target_location_1 = null;
    //当前选择的保护床方块层数
    private int protectBed = 1;
    //左1层床保护方块
    private Material LeftFirstBedBlock = Material.AIR;
    //左2层床保护方块
    private Material LeftSecondBedBlock = Material.WHITE_WOOL;
    //左3层床保护方块
    private Material LeftThirdBedBlock = Material.WHITE_WOOL;

    //右1层床保护方块
    private Material RightFirstBedBlock = Material.AIR;
    //右2层床保护方块
    private Material RightSecondBedBlock = Material.WHITE_WOOL;
    //右3层床保护方块
    private Material RightThirdBedBlock = Material.WHITE_WOOL;
    //将要破坏的层数
    private int destroyBedBlock = 1;
    private ArmorStand marker;
    private final int order;
    //经济
    private int money = 8;
    //是否拥有床
    private boolean has_bed = true;
    //行动力
    private int action = 1;
    //当前血量
    private int health = 100;
    //临时战力
    private int temporary_power = 0;
    private boolean needSpawn = false;

    public PlayerData(Player player) {
        this.player = player;
        order = order_g++;
        Team team = switch (order) {
            case 1 -> red;
            case 2 -> green;
            case 3 -> blue;
            default -> yellow;
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
                ar.setCustomNameVisible(true);
                ar.customName(Message.rMsg("<rainbow>%s的棋子".formatted(player.getName())));
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

    /**
     * @param money 传入的钱
     * @return 0或8
     */
    public static int finalMoney(int money) {
        return money == 0 ? 0 : Math.max(money, 8);
    }

    public static int getPower(Player p) {
        return players_data.get(p).getPower() + new Random().nextInt(1, 7);
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

    public void addTemporaryPower(int amount) {
        temporary_power += amount;
    }

    public void addAction(int amount) {
        action += amount;
    }

    public void resetTemporaryPower() {
        temporary_power = 0;
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
        target_location_1 = null;
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
        if (amount == 1) {
            return LeftFirstBedBlock;
        }
        if (amount == 2) {
            return LeftSecondBedBlock;
        }
        if (amount == 3) {
            return LeftThirdBedBlock;
        }
        if (amount == 4) {
            return RightThirdBedBlock;
        }
        if (amount == 5) {
            return RightSecondBedBlock;
        }
        if (amount == 6) {
            return RightFirstBedBlock;
        }
        return Material.AIR;
    }

    public void placeBedBlock(Material material) {
        if (protectBed == 1) {
            LeftFirstBedBlock = material;
        }
        if (protectBed == 2) {
            LeftSecondBedBlock = material;
        }
        if (protectBed == 3) {
            LeftThirdBedBlock = material;
        }
        if (protectBed == 4) {
            RightThirdBedBlock = material;
        }
        if (protectBed == 5) {
            RightSecondBedBlock = material;
        }
        if (protectBed == 6) {
            RightFirstBedBlock = material;
        }
    }

    public int getPower() {
        return (getMaxPower() * health / 100) + temporary_power;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int amount) {
        health = amount;
    }

    public boolean hasBed() {
        return has_bed;
    }

    public boolean needRespawn() {
        return needSpawn;
    }

    public void respawn() {
        map.islands.stream().filter(i -> i instanceof Bed b && b.getOrder() == order).findFirst().ifPresent(island -> {
            marker.teleport(GameMap.getLocation(island));
            map.moveTo(player, island);
        });
        needSpawn = false;
        player.sendMessage(Message.rMsg("             <green>消耗 ① 行动点你复活了"));
        action = 0;
    }

    public int getMaxPower() {
        return 2 + items.stream().mapToInt(Card::power).sum() + equipments.stream().mapToInt(Card::power).sum();
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
        if (has_bed) {
            has_bed = false;
            changeSidebarEntries(5 - order, "%s队床:§4 ✘".formatted(switch (order) {
                case 1 -> "§c红";
                case 2 -> "§a绿";
                case 3 -> "§9蓝";
                case 4 -> "§e黄";
                default -> "";
            }));
            Bukkit.broadcast(Message.rMsg("       <red>%s<red> <bold>的床被破坏!".formatted(player.getName())));
        }
    }

    /**
     * @param killer 击杀者
     * @return 击杀者获得的钱
     */
    public int die(Player killer) {
        var finalMoney = 0;
        location.players.remove(player);
        if (player.equals(killer)) {
            Bukkit.broadcast(Component.text("               %s自杀了".formatted(player.getName())));
            money /= 4;
            action = 0;
        } else {
            Bukkit.broadcast(Component.text("               %s被%s杀了".formatted(player.getName(), killer.getName())));
            finalMoney = money;
            money = 0;
            items.removeIf(Card::CanDrop);
            equipments.removeIf(Card::CanDrop);
            resetInventoryItems();
        }
        getMarker().teleport(new Location(player.getWorld(), 0, 255, 0));
        if (!has_bed) {
            //最终击杀
            Bukkit.broadcast(Message.rMsg("             <red>%s</red> <bold>被最终淘汰!".formatted(player.getName())));
            player.teleport(new Location(player.getWorld(), 0, 22, 0));
            player.showTitle(Title.title(Message.rMsg("<aqua>观战"), Message.rMsg("<rainbow>--------")));
        }
        needSpawn = true;
        //检测结束
        if (players_data.values().stream().filter(pd -> !pd.has_bed && pd.needSpawn).count() == 3) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    end();
                }
            }.runTaskLater(BedWars.instance, 1);
        }
        return finalMoney;
    }


    public int getOrder() {
        return order;
    }

    public List<ItemStack> getActions() {
        List<ItemStack> items = new ArrayList<>();
        //移动 60007
        items.add(ItemCreator.create(Material.PAPER).data(60007).name(Component.text("§a移动")).getItem());
        //搭路 60003
        if (location instanceof Island) {
            items.add(ItemCreator.create(Material.PAPER).data(60003).name(Component.text("§b搭路")).getItem());
        }
        //选择目标 60009
        //pvp 60002
        if (location.players.size() > 1) {
            items.add(ItemCreator.create(Material.PAPER).data(60009).name(Component.text("§7选择目标")).getItem());
            items.add(ItemCreator.create(Material.PAPER).data(60002).name(Component.text("§cPVP")).getItem());
        }
        //使用道具 60008
        items.add(ItemCreator.create(Material.PAPER).data(60008).name(Component.text("§d使用道具")).getItem());
        //破坏60006
        items.add(ItemCreator.create(Material.PAPER).data(60006).name(Component.text("§1破坏桥")).getItem());
        //商店60005、床60004  他人60010
        if (location instanceof Bed) {
            //商店
            items.add(ItemCreator.create(Material.PAPER).data(60005).name(Component.text("§6商店")).getItem());
            items.add(ItemCreator.create(Material.PAPER).data(60010).name(Component.text("§3与床互动")).getItem());
        }
        //跳过回合
        items.add(ItemCreator.create(Material.PAPER).data(60011).name(Component.text("§9跳过回合")).getItem());
        return items;
    }

    public ArmorStand getMarker() {
        return marker;
    }

    public void resetInventoryItems() {
        PlayerData playerData = players_data.get(player);
        List<Card> equipments = playerData.equipments;
        for (int i = 0; i < equipments.size(); i++) {
            Card card = equipments.get(i);
            player.getInventory().setItem(9 + i, ItemCreator.create(Material.PAPER).name(card
                            .Name())
                    .amount(1)
                    .data(card.CustomModelData())
                    .lore(card.Introduction())
                    .hideAttributes().getItem());
        }
        List<Card> items = playerData.items;
        for (int i = 0; i < items.size(); i++) {
            Card card = items.get(i);
            player.getInventory().setItem(35 - i, ItemCreator.create(Material.PAPER).name(card
                            .Name())
                    .amount(1)
                    .data(card.CustomModelData())
                    .lore(card.Introduction())
                    .hideAttributes().getItem());
        }
    }
}