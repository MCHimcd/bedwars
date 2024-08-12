package mc.bedwars.game;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.factory.Message;
import mc.bedwars.game.card.boost.HealingSpring;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.resource.Bed;
import mc.bedwars.game.map.node.island.resource.Resource;
import mc.bedwars.menu.MainMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.time.Duration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static mc.bedwars.BedWars.*;
import static mc.bedwars.game.map.GameMap.materials;
import static org.bukkit.attribute.Attribute.GENERIC_JUMP_STRENGTH;

public final class GameState {
    public static boolean started = false;
    public static int turn;
    public static Map<Player, PlayerData> players_data = new Hashtable<>();
    public static GameMap map = null;
    public static int order = 1;

    public static void start(List<Player> players) {

        if (players.size() > 4) {
            Bukkit.broadcast(Component.text("            §c游戏需要至多4名玩家参与！"));
            return;
        }
        if (players.size() <= 1) {
            Bukkit.broadcast(Component.text("            §c游戏需要至少2名玩家参与！"));
            return;
        }
        if (players.size() <= 3) changeSidebarEntries(1, "§e黄队床:§4 ✘");
        if (players.size() <= 2) changeSidebarEntries(2, "§9蓝队床:§4 ✘");

        new StartBukkitRunnable(players).runTaskTimer(instance, 0, 1);
    }

    public static void end() {
        //结束
        players_data.keySet().stream().filter(p -> players_data.get(p).hasBed()).findFirst().ifPresent(winner -> {
            Bukkit.broadcast(Message.rMsg("      <gold><bold> %s <white>获得了最终的胜利".formatted(winner.getName())));
            winner.playSound(winner, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1f, 1f);
        });
        reset();
    }

    public static void reset() {
        turn = 0;
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR_TEAM_AQUA);
        changeSidebarEntries(5, "§6-------------§1");
        changeSidebarEntries(4, "§c红队床:§2 ✔");
        changeSidebarEntries(3, "§a绿队床:§2 ✔");
        changeSidebarEntries(2, "§9蓝队床:§2 ✔");
        changeSidebarEntries(1, "§e黄队床:§2 ✔");
        changeSidebarEntries(0, "§6-------------§2");
        //重置
        players_data.values().forEach(p -> p.getMarker().remove());
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            resetPlayer(player);
            player.sendMessage(Component.text("               §c§l 游戏已重置!"));
        });
        players_data.clear();
        order = 0;
        PlayerData.resetOrderG();
        if (map != null) map.markers.keySet().forEach(Entity::remove);
        map = new GameMap(Bukkit.getWorld("world"));
        started = false;
        MainMenu.prepared.clear();
        MainMenu.start_tick = -1;
        TickRunner.ending = false;
    }

    public static void resetPlayer(Player player) {
        var team = main_scoreboard.getEntityTeam(player);
        if (team != null) {
            team.removeEntity(player);
        }
        player.hideBossBar(bossbar);
        player.teleport(new Location(player.getWorld(), 0, 66, 0));
        player.getInventory().clear();
        player.clearActivePotionEffects();
        player.setGameMode(GameMode.ADVENTURE);
        Objects.requireNonNull(player.getAttribute(GENERIC_JUMP_STRENGTH)).setBaseValue(0.5);

        player.getInventory().addItem(ItemCreator.create(Material.PAPER).data(60000).name(Message.rMsg("主菜单", NamedTextColor.GOLD)).getItem());
    }

    public static void nextPlayer() {
        if (++order == players_data.size() + 1) {
            nextTurn();
            return;
        }
        players_data.forEach((p, d) -> {
            p.getInventory().clear();
            d.resetInventoryItems();
            if (d.getOrder() == order) {
                if (d.needRespawn()) {
                    d.respawn();
                } else {
                    Bukkit.broadcast(Message.rMsg("               <gold>现在轮到<aqua><bold> %s <reset><gold>的回合了".formatted(p.getName())));
                    bossbar.name(Message.rMsg("<aqua><bold>%s</bold>回合<gold>  当前轮次:</gold><blue><bold>%s".formatted(p.getName(), turn)));
                    p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 2f);
                    var is = d.getActions();
                    for (int i = 0; i < is.size(); i++) {
                        p.getInventory().setItem(i, is.get(i));
                    }
                }
                if (d.location instanceof Resource r) {
                    r.giveMoney(p);
                }
            }
        });
        for (var island : map.islands) {
            if (island instanceof Resource r) {
                map.markers.forEach((m, n) -> {
                    if (r.equals(n)) {
                        m.text(Component.text("%s岛".formatted(island.getType())).append(Component.text("\n%s存有数量:%d".formatted(island.getType().substring(2), r.getAmount()), NamedTextColor.RED)));
                    }
                });
            }
        }
    }

    public static void nextTurn() {
        order = 0;
        turn++;
        map.islands.forEach(node -> {
            if (node instanceof Resource r) r.generate();
        });
        players_data.forEach((it, playerData) -> {
            //重置临时战力
            playerData.resetTemporaryPower();
            //血量低于50回血
            if (playerData.getHealth() < 50) playerData.setHealth(playerData.getHealth() + 10);
            //重置行动力
            playerData.resetAction();
            //重置目标
            playerData.resetTarget();
            var Hs = playerData.items.stream().filter(item -> item instanceof HealingSpring).findFirst();
            //生命泉水
            if (Hs.isPresent()) {
                if (playerData.location instanceof Bed bed && bed.getOrder() == playerData.getOrder()) {
                    playerData.addTemporaryPower(3);
                }
            }
        });
        nextPlayer();
    }

    public static void pvp(Player attacker, Player target) {
        if (attacker.equals(target)) return;
        int power1 = PlayerData.getPower(attacker);
        int power2 = PlayerData.getPower(target);
        while (power1 == power2) {
            power1 = PlayerData.getPower(attacker);
            power2 = PlayerData.getPower(target);
        }
        attacker.sendMessage(Message.rMsg("          <green>你的战力:%d   <red>对方战力:%d".formatted(power1, power2)));
        target.sendMessage(Message.rMsg("          <green>你的战力:%d   <red>对方战力:%d".formatted(power2, power1)));
        var winner = power1 > power2 ? attacker : target;
        var loser = power1 > power2 ? target : attacker;
        loser.sendMessage(Message.rMsg("          <gray>你输了~"));
        winner.sendMessage(Message.rMsg("          <gold>你赢了~"));
        players_data.get(winner).hurt(players_data.get(loser).getPower());
        var ld = players_data.get(loser);
        var total_money = players_data.get(loser).die(winner);
        var final_dead = !ld.hasBed() && ld.needRespawn();
        if (total_money <= 16) {
            players_data.get(winner).addMoney(PlayerData.finalMoney(total_money));
        } else if (total_money <= 32) {
            PlayerData w = players_data.get(winner);
            w.addMoney(16);
            if (final_dead) {
                w.addMoney(PlayerData.finalMoney(total_money - 16));
            } else {
                ld.addMoney(PlayerData.finalMoney(total_money - 16));
            }
        }
        ld.addAction(-1);
        players_data.get(winner).addAction(-1);
    }

    private static class StartBukkitRunnable extends BukkitRunnable {
        private final List<Player> players;
        int t;
        int a;

        public StartBukkitRunnable(List<Player> players) {
            this.players = players;
            t = 0;
            a = 0;
        }

        @Override
        public void run() {
            t++;
            if (t % 10 == 0) {
                a++;
                players.forEach(player -> {
                    player.playSound(player, Sound.UI_BUTTON_CLICK, .5f, 1f);
                    player.showTitle(Title.title(Message.rMsg("<rainbow> --游戏加载中--"),
                            Message.rMsg("<gold>" + "■".repeat(t / 10) + "<white>" + "□".repeat(10 - t / 10)),
                            Title.Times.times(Duration.ZERO, Duration.ofMillis(1100), Duration.ZERO)));
                });
            }
            if (t <= 81) {
                //重置地图
                for (int i = -40; i < 41; i++) {
                    var b = new Location(Bukkit.getWorld("world"), i, 0, t - 41).getBlock();
                    if (b.getType() != Material.BARRIER && materials.contains(b.getType()))
                        b.setType(Material.BARRIER);
                }
            }
            if (t >= 100) {
                cancel();
                //开始
                players.forEach(player -> {
                    var pd = new PlayerData(player);
                    player.setGameMode(GameMode.ADVENTURE);
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, .5f);
                    player.teleport(pd.getMarker().getLocation().clone().add(0, 21, 0));
                    player.showBossBar(bossbar);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 5, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 5, false, false));
                });
                sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
                started = true;
                MainMenu.prepared.clear();
                nextTurn();
            }
        }
    }
}