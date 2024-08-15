package mc.bedwars;

import mc.bedwars.Command.resetCmd;
import mc.bedwars.Command.start;
import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.TickRunner;
import mc.bedwars.menu.MainMenu;
import mc.bedwars.menu.SlotMenu;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.*;

public final class BedWars extends JavaPlugin implements Listener {
    public static BossBar bossbar;
    public static JavaPlugin instance;
    public static Scoreboard main_scoreboard;
    public static Objective sidebar;
    public static Team red, green, blue, yellow;
    public static List<String> sidebar_entries = new ArrayList<>();

    public static void changeSidebarEntries(int index, String s) {
        sidebar.getScore(sidebar_entries.get(index)).resetScore();
        sidebar.getScore(s).setScore(index);
        sidebar_entries.set(index, s);
    }

    @Override
    public void onDisable() {
        map.markers.keySet().forEach(Entity::remove);
        players_data.forEach((p, pd) -> {
            p.hideBossBar(bossbar);
            pd.getMarker().remove();
        });
        for (String s : sidebar_entries) {
            sidebar.getScore(s).resetScore();
        }
    }

    @Override
    public void onEnable() {


        instance = this;

        //noinspection DataFlowIssue
        Bukkit.getWorld("world").getEntities().stream().filter(e -> !(e instanceof Player)).forEach(Entity::remove);
        summonDecorationEntities();

        initScoreboard();

        try {
            Objects.requireNonNull(Bukkit.getPluginCommand("reset")).setExecutor(new resetCmd());
            Objects.requireNonNull(Bukkit.getPluginCommand("start")).setExecutor(new start());
        } catch (NullPointerException e) {
            getLogger().warning(e.getMessage());
        }

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        new TickRunner().runTaskTimer(this, 0, 1);

        reset();
    }

    private void initScoreboard() {
        bossbar = BossBar.bossBar(Component.empty(), 1, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
        main_scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        sidebar = main_scoreboard.getObjective("sidebar");
        if (sidebar == null) {
            sidebar = main_scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, rMsg("<yellow>BEDWARS"));
        }
        for (int i = 0; i < 7; i++) {
            sidebar_entries.add("null");
        }
        red = main_scoreboard.getTeam("red");
        if (red == null) {
            red = main_scoreboard.registerNewTeam("red");
            red.color(NamedTextColor.RED);
        }
        green = main_scoreboard.getTeam("green");
        if (green == null) {
            green = main_scoreboard.registerNewTeam("green");
            green.color(NamedTextColor.GREEN);
        }
        blue = main_scoreboard.getTeam("blue");
        if (blue == null) {
            blue = main_scoreboard.registerNewTeam("blue");
            blue.color(NamedTextColor.BLUE);
        }
        yellow = main_scoreboard.getTeam("yellow");
        if (yellow == null) {
            yellow = main_scoreboard.registerNewTeam("yellow");
            yellow.color(NamedTextColor.YELLOW);
        }
    }

    private void summonDecorationEntities() {
        var w = Bukkit.getWorld("world");
        assert w != null;
        w.spawn(new Location(w, 4.5, 65, 13.7), ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setInvisible(true);
            armorStand.teleport(armorStand.getLocation().setDirection(new Vector(0, 0, -1)));
            armorStand.getEquipment().setHelmet(ItemCreator.create(Material.PAPER).data(90018).getItem());
        });
        w.spawn(new Location(w, 4.5, 67.25, 13.7), TextDisplay.class, textDisplay -> {
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.text(rMsg("极熠-蓝空"));
            textDisplay.setBackgroundColor(Color.fromARGB(0));
        });

        w.spawn(new Location(w, 3, 64.85, 13), ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setInvisible(true);
            armorStand.teleport(armorStand.getLocation().setDirection(new Vector(-1, 0, -1)));
            armorStand.getEquipment().setHelmet(ItemCreator.create(Material.PAPER).data(90019).getItem());
        });
        w.spawn(new Location(w, 3, 64.85 + 1.75, 13), TextDisplay.class, textDisplay -> {
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.text(rMsg("极熠-马某"));
            textDisplay.setBackgroundColor(Color.fromARGB(0));
        });

        w.spawn(new Location(w, 1.5, 64, 13.5), ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setInvisible(true);
            armorStand.teleport(armorStand.getLocation().setDirection(new Vector(0, 0, -1)));
            armorStand.getEquipment().setHelmet(ItemCreator.create(Material.PAPER).data(90020).getItem());
        });
        w.spawn(new Location(w, 1.5, 66.25, 13.5), TextDisplay.class, textDisplay -> {
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.text(rMsg("<aqua>极熠-红雷"));
            textDisplay.setBackgroundColor(Color.fromARGB(-5746120));
        });

        w.spawn(new Location(w, 0, 64, 13.5), ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setInvisible(true);
            armorStand.teleport(armorStand.getLocation().setDirection(new Vector(0, 0, -1)));
            armorStand.getEquipment().setHelmet(ItemCreator.create(Material.PAPER).data(90021).getItem());
        });
        TickRunner.himcd = w.spawn(new Location(w, 0, 66.25, 13.5), TextDisplay.class, textDisplay -> {
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.text(rMsg("极熠-Himcd"));
            textDisplay.setBackgroundColor(Color.fromARGB(-9868951));
            textDisplay.addScoreboardTag("himcd");
        });

        w.spawn(new Location(w, -2.2, 65, 13), ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setInvisible(true);
            armorStand.teleport(armorStand.getLocation().setDirection(new Vector(0.2, 0, -1)));
            armorStand.getEquipment().setHelmet(ItemCreator.create(Material.PAPER).data(90022).getItem());
        });
        w.spawn(new Location(w, -2.2, 67.25, 13), TextDisplay.class, textDisplay -> {
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.text(rMsg("DummyTeam-某人"));
            textDisplay.setBackgroundColor(Color.fromARGB(0));
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var p = event.getPlayer();
        if (started) {
            var player = players_data.keySet().stream().filter(pl -> pl.equals(p)).findFirst();
            if (player.isPresent()) {
                p.showBossBar(bossbar);
            } else resetPlayer(p);
        } else {
            resetPlayer(p);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var p = event.getPlayer();
        MainMenu.removePrepared(p);
    }


    @EventHandler
    public void onHurt(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) e.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getInventory().getHolder() instanceof SlotMenu m) m.handleClick(e.getSlot());
        else if (started && Objects.requireNonNull(e.getInventory().getHolder()).equals(player)) {
            e.setCancelled(true);
            if (e.getClick() == ClickType.RIGHT) {
                var it = e.getCurrentItem();
                if (it == null) return;
                if (it.getType() == Material.PAPER) player.getInventory().setItemInOffHand(it);
            }
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (started) e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().hasCustomModelData()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (started) {
            var pd = players_data.get((Player) event.getPlayer());
            if (pd != null) pd.resetInventoryItems();
        }
    }
}