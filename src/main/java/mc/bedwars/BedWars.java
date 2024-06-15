package mc.bedwars;

import mc.bedwars.Command.start;
import mc.bedwars.Command.test;
import mc.bedwars.factory.ItemCreator;
import mc.bedwars.factory.Message;
import mc.bedwars.game.GameState;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.TickRunner;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.equips.IronAxe;
import mc.bedwars.game.card.equips.Pickaxe;
import mc.bedwars.game.card.equips.Scissors;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.game.map.node.island.resource.Bed;
import mc.bedwars.menu.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Objects;

import static mc.bedwars.game.GameState.*;

public final class BedWars extends JavaPlugin implements Listener {

    public static JavaPlugin instance;
    public static Scoreboard main_scoreboard;
    public static Team red, green,blue,yellow;

    @Override
    public void onEnable() {
        instance = this;
        main_scoreboard=Bukkit.getScoreboardManager().getMainScoreboard();
        red=main_scoreboard.getTeam("red");
        if (red == null) {
            red=main_scoreboard.registerNewTeam("red");
            red.color(NamedTextColor.RED);
        }
        green=main_scoreboard.getTeam("green");
        if (green == null) {
            green=main_scoreboard.registerNewTeam("green");
            green.color(NamedTextColor.GREEN);
        }
        blue=main_scoreboard.getTeam("blue");
        if (blue == null) {
            blue=main_scoreboard.registerNewTeam("blue");
            blue.color(NamedTextColor.BLUE);
        }
        yellow=main_scoreboard.getTeam("yellow");
        if (yellow == null) {
            yellow=main_scoreboard.registerNewTeam("yellow");
            yellow.color(NamedTextColor.YELLOW);
        }
        try {
            Objects.requireNonNull(Bukkit.getPluginCommand("test")).setExecutor(new test());
            Objects.requireNonNull(Bukkit.getPluginCommand("start")).setExecutor(new start());
        } catch (NullPointerException e) {
            getLogger().warning(e.getMessage());
        }

        getServer().getPluginManager().registerEvents(this, this);
        new TickRunner().runTaskTimer(this, 0, 1);
        reset();
    }

    @Override
    public void onDisable() {
        map.markers.keySet().forEach(Entity::remove);
        players_data.values().forEach(p->p.getMarker().remove());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var p = event.getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        var item = e.getItem();
        var p = e.getPlayer();
        if (item != null) {
            if (!item.getItemMeta().hasCustomModelData()) return;
            var id = item.getItemMeta().getCustomModelData();
            var pd = players_data.get(p);
            switch (id) {
                case 60009 -> //选择目标菜单
                        p.openInventory(new ChoosePlayerMenu(
                                p, pd.location.players.stream().filter(player -> !player.equals(p)).toList(),
                                (it, pl) -> {
                                    SkullMeta meta = (SkullMeta) it.getItemMeta();
                                    var target = (Player) meta.getOwningPlayer();
                                    pd.setTarget(target);
                                    p.closeInventory();
                                    assert target != null;
                                    p.showTitle(Title.title(Message.rMsg("<aqua>你选择的目标为:"),Message.rMsg("<rainbow>%s".formatted(target.getName()))));
                                    p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                }
                        ).getInventory());
                case 60007 -> {
                    //移动
                    if (pd.target_location != null) {
                        if (!map.move(p, pd.location, pd.target_location)) {
                            p.showTitle(Title.title(Message.rMsg("<aqua>当前岛屿方向没路可走"),Message.rMsg(".")));
                            p.playSound(p, Sound.ENTITY_VILLAGER_NO,1f,1f);
                        }
                    } else {
                        p.showTitle(Title.title(Message.rMsg("<aque>请选择一个岛屿"),Message.rMsg(".")));
                        p.playSound(p, Sound.ENTITY_VILLAGER_NO,1f,1f);
                    }
                }
                case 60003 -> {
                    //搭路
                    if (pd.target_location != null) {
                        Island i1 = (Island) pd.location;
                        Island i2 = (Island) pd.target_location;
                        if (Math.abs(i1.getX() - i2.getX()) == 1 || Math.abs(i1.getY() - i2.getY()) == 1){
                            p.openInventory(new BlockMenu(p).getInventory());
                        }else {
                            p.showTitle(Title.title(Message.rMsg("<aqua>请选择一个就近岛屿"),Message.rMsg(".")));
                        }
                    }else {
                        p.showTitle(Title.title(Message.rMsg("<aqua>请选择一个就近岛屿"),Message.rMsg(".")));
                        p.playSound(p, Sound.ENTITY_VILLAGER_NO,1f,1f);
                    }
                }
                //使用道具
                case 60008 -> p.openInventory(new CardMenu(p, pd.items).getInventory());
                case 60002 -> {
                    //pvp
                    //除被玩家选中的人  以外  岛上其他人可以选择加入其中一方，或者旁观
                    var t = pd.getTarget();
                    if (t != null) {
                        p.playSound(p,Sound.ENTITY_PLAYER_ATTACK_SWEEP,1f,1.5f);
                        pd.getTarget().playSound(pd.getTarget(),Sound.ENTITY_PLAYER_ATTACK_SWEEP,1f,1.5f);
                        p.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(Message.rMsg("         %s和%s进行了一场<red>pvp".formatted(p.getName(), t.getName()))));
                        GameState.pvp(pd.location.players, p, t);
                        pd.addAction(-1);
                    }
                }
                case 60006 -> {
                    //破坏
                    if (pd.target_location != null) {
                        Island i1 = (Island) pd.location;
                        Island i2 = (Island) pd.target_location;
                        if (Math.abs(i1.getX() - i2.getX()) == 1 || Math.abs(i1.getY() - i2.getY()) == 1) {
                            map.roads.stream().filter(road -> road.hasNode(pd.location)).findFirst().ifPresent(
                                    road -> {
                                        if (switch (road.getMaterial()) {
                                            case END_STONE ->
                                                    pd.equipments.stream().anyMatch(em -> em instanceof Pickaxe);
                                            case WHITE_WOOL ->
                                                    pd.equipments.stream().anyMatch(em -> em instanceof Scissors);
                                            case CRIMSON_PLANKS ->
                                                    pd.equipments.stream().anyMatch(em -> em instanceof IronAxe);
                                            default -> false;
                                        }) {
                                            map.breakRoad(p, road);
                                            p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                            pd.addAction(-1);
                                        }else {
                                            p.showTitle(Title.title(Message.rMsg("<aqua>你没有破坏这座桥的工具"),Message.rMsg(".")));
                                            p.playSound(p,Sound.ENTITY_VILLAGER_NO,1f,1f);
                                        }
                                    }
                            );
                        }else {
                            p.showTitle(Title.title(Message.rMsg("<aqua> 请选择一个就近岛屿 "),Message.rMsg(".")));
                            p.playSound(p,Sound.ENTITY_VILLAGER_NO,1f,1f);
                        }
                    }
                }
                case 60005 -> //商店
                {
                    p.openInventory(new ShopMenu(p).getInventory());
                    p.playSound(p,Sound.ENTITY_VILLAGER_TRADE,1f,1.5f);
                }
                case 60010 -> {
                    //破坏床
                    if (pd.location instanceof Bed b) {
                        if (b.getOrder() != pd.getOrder()) {
                            players_data.values().stream().filter(pld -> b.getOrder() == pld.getOrder()).findFirst().ifPresent(pld -> {
                                p.openInventory(new ChooseDestoryBedBlockMenu(p).getInventory());
                            });
                        }
                    }
                }
                case 60004 ->{//建床
                    p.openInventory(new ChoosePlaceBedBlockMenu(p).getInventory());
                    p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                }
                case 60011 -> //跳过回合
                {
                    map.move(p,pd.location,pd.location);
                    p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                    p.playSound(p,Sound.UI_BUTTON_CLICK,1f,.5f);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if(e.getInventory().getHolder() instanceof SlotMenu m) m.handleClick(e.getSlot());
        else if(started&&e.getInventory().getHolder().equals(player)){
            player.getInventory().setItemInOffHand(e.getCursor());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().hasCustomModelData()) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void openInv(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        PlayerData playerData = players_data.get(player);
        List<Card> equipments = playerData.equipments;
        for (int i = 0; i < equipments.size(); i++) {
            Card card = equipments.get(i);
            player.getInventory().setItem(9+i, ItemCreator.create(Material.PAPER).name(card
                            .Name())
                    .amount(1)
                    .data(card.CustomModelData())
                    .lore(card.Introduction())
                    .hideAttributes().getItem());
        }
        List<Card> items = playerData.items;
        for (int i = 0; i < items.size(); i++) {
            Card card = items.get(i);
            player.getInventory().setItem(35-i, ItemCreator.create(Material.PAPER).name(card
                            .Name())
                    .amount(1)
                    .data(card.CustomModelData())
                    .lore(card.Introduction())
                    .hideAttributes().getItem());
        }
    }
}