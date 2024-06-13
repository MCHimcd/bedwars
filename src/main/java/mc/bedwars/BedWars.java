package mc.bedwars;

import mc.bedwars.Command.test;
import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.GameState;
import mc.bedwars.game.TickRunner;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.player.PlayerData;
import mc.bedwars.menu.CardMenu;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
        // Plugin shutdown logic
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
        if (item == null) {

        } else {
            if (!item.getItemMeta().hasCustomModelData()) return;
            var id = item.getItemMeta().getCustomModelData();
            var pd = players_data.get(p);
            switch (id) {
                case 1 -> {
                    //卡片菜单
                    p.openInventory(new CardMenu(p, pd.items).getInventory());
                }
                case 60009 -> {
                    //选择目标菜单
                    p.openInventory(new ChoosePlayerMenu(
                            p, pd.location.players.stream().filter(player -> !player.equals(p)).toList(),
                            (it, pl) -> {
                                SkullMeta meta = (SkullMeta) it.getItemMeta();
                                var target = (Player) meta.getOwningPlayer();
                                pd.setTarget(target);
                                p.closeInventory();
                                assert target != null;
                                p.sendMessage(Component.text("<S>     你选择了%s".formatted(target.getName())));
                            }
                    ).getInventory());
                }
                case 60007 -> {
                    //移动

                }
                case 60003 -> {
                    //搭路
                }
                case 60008 -> {
                    p.openInventory(new CardMenu(p, pd.items).getInventory());
                }
                case 60002 -> {
                    //pvp
                    var t = pd.getTarget();
                    if (t != null) {
                        p.getServer().getOnlinePlayers().forEach(player -> {
                            player.sendMessage(Component.text("<S>     %s和%s进行了一场pvp;".formatted(p.getName(),t.getName())));
                        });
                        GameState.pvp(pd.location.players, p, t);
                    }
                }
                //除被玩家选中的人  以外  岛上其他人可以选择加入其中一方，或者旁观
                case 60006 -> {
                    //破坏
                }
                case 60005 -> {
                    //商店
                }
                case 60004 -> {
                    //床
                }
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