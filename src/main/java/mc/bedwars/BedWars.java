package mc.bedwars;

import mc.bedwars.Command.test;
import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.player.PlayerData;
import mc.bedwars.menu.CardMenu;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

import static mc.bedwars.game.GameState.players_data;
import static mc.bedwars.game.GameState.reset;

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
        if (item == null || !item.getItemMeta().hasCustomModelData()) return;
        var id = item.getItemMeta().getCustomModelData();
        switch (id) {
            case 1 -> {
                //卡片菜单
                var pd = players_data.get(p);
                p.openInventory(new CardMenu(p, pd.items).getInventory());
            }
        }
    }
    @EventHandler
    public void openInv(InventoryOpenEvent event){
        Player player = (Player) event.getPlayer();
        PlayerData playerData = players_data.get(player);
        List<Card> equipments = playerData.equipments;
        for (int i = 0; i< equipments.size(); i++){
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