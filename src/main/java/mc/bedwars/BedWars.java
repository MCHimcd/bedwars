package mc.bedwars;

import mc.bedwars.Command.test;
import mc.bedwars.factory.ItemCreator;
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

import java.util.List;
import java.util.Objects;

import static mc.bedwars.game.GameState.*;

public final class BedWars extends JavaPlugin implements Listener {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        try {
            Objects.requireNonNull(Bukkit.getPluginCommand("test")).setExecutor(new test());
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
                case 1 -> //卡片菜单
                        p.openInventory(new CardMenu(p, pd.items).getInventory());
                case 60009 -> //选择目标菜单
                        p.openInventory(new ChoosePlayerMenu(
                                p, pd.location.players.stream().filter(player -> !player.equals(p)).toList(),
                                (it, pl) -> {
                                    SkullMeta meta = (SkullMeta) it.getItemMeta();
                                    var target = (Player) meta.getOwningPlayer();
                                    pd.setTarget(target);
                                    p.closeInventory();
                                    assert target != null;
                                    p.sendMessage(Component.text("<S>     你选择了%s".formatted(target.getName())));
                                    p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                }
                        ).getInventory());
                case 60007 -> {
                    //移动
                    if (pd.target_location != null) {
                        if (!map.move(p, pd.location, pd.target_location)) {
                            p.sendMessage(Component.text("无路"));
                            p.playSound(p, Sound.ENTITY_VILLAGER_NO,1f,1f);
                        }
                    } else {
                        p.sendMessage("无目标");
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
                        }
                    }
                }
                //使用道具
                case 60008 -> p.openInventory(new CardMenu(p, pd.items).getInventory());
                case 60002 -> {
                    //pvp
                    //除被玩家选中的人  以外  岛上其他人可以选择加入其中一方，或者旁观
                    var t = pd.getTarget();
                    if (t != null) {
                        p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                        p.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(Component.text("<S>     %s和%s进行了一场pvp;".formatted(p.getName(), t.getName()))));
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
                                            case Material.END_STONE ->
                                                    pd.equipments.stream().anyMatch(em -> em instanceof Pickaxe);
                                            case WHITE_WOOL ->
                                                    pd.equipments.stream().anyMatch(em -> em instanceof Scissors);
                                            case Material.CRIMSON_PLANKS ->
                                                    pd.equipments.stream().anyMatch(em -> em instanceof IronAxe);
                                            default -> false;
                                        }) {
                                            map.breakRoad(p, road);
                                            p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                            pd.addAction(-1);
                                        }
                                    }
                            );

                        }
                    }
                }
                case 60005 -> //商店
                        p.openInventory(new ShopMenu(p).getInventory());
                case 60010 -> {
                    //破坏床
                    if (pd.location instanceof Bed b) {
                        if (b.getOrder() != pd.getOrder()) {
                            players_data.values().stream().filter(pld -> b.getOrder() == pld.getOrder()).findFirst().ifPresent(pld -> {
                                for (int j = 0; j < 4; j++) {
                                    if (pld.protectBedBlockMaterial(j) != Material.AIR) {
                                        pd.setDestroyBedBlock(j);
                                        p.openInventory(new DestoryBedMenu(p, pd.equipments).getInventory());
                                        p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                        break;
                                    }
                                    if (j == 3) pld.destroyBed();
                                }
                            });
                        }
                    }
                }
                case 60004 -> //建床
                        p.openInventory(new ChoosePlaceBedBlockMenu(p).getInventory());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player) || !(e.getInventory().getHolder() instanceof SlotMenu m)) return;
        m.handleClick(e.getSlot());
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
            player.getInventory().setItem(i, ItemCreator.create(Material.PAPER).name(card
                            .Name())
                    .amount(card.itemMaxCount())
                    .data(card.CustomModelData())
                    .lore(card.Introduction())
                    .hideAttributes().getItem());
        }
    }
}