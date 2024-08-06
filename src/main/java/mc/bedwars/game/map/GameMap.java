package mc.bedwars.game.map;

import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Blocks.Wool;
import mc.bedwars.game.card.props.EnderPearl;
import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.game.map.node.island.Grass;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.game.map.node.island.Platform;
import mc.bedwars.game.map.node.island.resource.*;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.bedwars.game.GameState.map;
import static mc.bedwars.game.GameState.players_data;

//游戏地图
public class GameMap {
    public static final List<Material> materials = new ArrayList<>(List.of(
            Material.WHITE_WOOL,
            Material.CRIMSON_PLANKS,
            Material.END_STONE,
            Material.OBSIDIAN,
            Material.BARRIER,
            Material.BLACK_STAINED_GLASS
    ));
    public final Map<TextDisplay, Node> markers = new HashMap<>();
    public final List<Road> roads = new ArrayList<>();
    /*
    金。绿。金
    。钻草钻。
    蓝草绿草红
    。钻草钻。
    金。黄。金
    */
    public List<Island> islands = new ArrayList<>(List.of(
            new Gold(0, 0),
            new Gold(0, 4),
            new Gold(4, 0),
            new Gold(4, 4),
            new Bed(0, 2, 3),
            new Bed(2, 0, 2),
            new Bed(2, 4, 4),
            new Bed(4, 2, 1),
            new Diamond(1, 1),
            new Diamond(3, 3),
            new Diamond(3, 1),
            new Diamond(1, 3),
            new Grass(2, 1),
            new Grass(1, 2),
            new Grass(3, 2),
            new Grass(2, 3),
            new Platform(0, 1),
            new Platform(0, 3),
            new Platform(1, 0),
            new Platform(3, 0),
            new Platform(1, 4),
            new Platform(3, 4),
            new Platform(4, 1),
            new Platform(4, 3),
            new Emerald(2, 2)
    ));

    public GameMap(World world) {
        //生成markers
        for (var i : islands) {
            markers.put(world.spawn(getLocation(i), TextDisplay.class, marker -> {
                var t = marker.getTransformation();
                marker.setTransformation(new Transformation(t.getTranslation().add(0, 5, 0), t.getLeftRotation(), t.getScale().mul(4), t.getRightRotation()));
                marker.setBillboard(Display.Billboard.CENTER);
                marker.setDefaultBackground(true);
                marker.text(Component.text("%s岛".formatted(i.getType())));
            }), i);
        }
    }

    public static void intoVoid(Player causer, Player p) {
        var pd = players_data.get(p);
        var ep = pd.items.stream().filter(item -> item instanceof EnderPearl).findFirst();
        if (ep.isPresent()) {
            Bukkit.broadcast(Component.text("          %s使用了末影珍珠防止掉落虚空;".formatted(p.getName())));
            ((EnderPearl) ep.get()).backHome(p);
        } else pd.die(causer);
    }

    public static Location getLocation(Island island) {
        return new Location(Bukkit.getWorld("world"), (island.getX() - 2) * 20 + 0.5, 1, (island.getY() - 2) * 20 + 0.5);
    }

    public static void replaceBlock(Location l1, Location l2, Material material) {
        l1.setY(0);
        l2.setY(0);
        var v1 = l2.clone().subtract(l1).toVector().normalize();
        for (; l1.distance(l2) > 1; l1.add(v1)) {
            var block = l1.getBlock();
            if (materials.contains(block.getType())) {
                var world = Bukkit.getWorld("world");
                assert world != null;
                if (material == Material.AIR) {
                    //破坏
                    world.spawnParticle(Particle.CLOUD, l1.clone().add(0, 1, 0), 100, 0.2, 0.2, 0.2, 0.4, null, true);
                    material = Material.BARRIER;
                } else {
                    //搭建
                    world.spawnParticle(Particle.END_ROD, l1.clone().add(0, 1, 0), 100, 0.2, 0.2, 0.2, 0.4, null, true);
                }
                block.setType(material);
            }
        }
    }

    public static Island getIsland(int x, int y) {
        var i = map.islands.stream().filter(island -> island.getX() == x && island.getY() == y).findFirst();
        return i.orElse(null);
    }

    public boolean tryMove(Player p, Node start, Node end) {
        PlayerData pd = players_data.get(p);
        var order = pd.getOrder();
        var dxz = order <= 2 ? (order - 3) * 0.5 : (order - 2) * 0.5;
        if (start.equals(end)) {
            start.players.remove(p);
            pd.die(p);
            return true;
        } else if (start instanceof Island island) {
            //在岛上
            var road = roads.stream().filter(r -> r.getMaterial() != Material.AIR && r.hasNode(start) && r.hasNode(end)).findFirst();
            if (road.isPresent()) {
                island.players.remove(p);
                moveTo(p, road.get());
                var l1 = GameMap.getLocation(island);
                var l2 = GameMap.getLocation((Island) end);
                var l3 = new Location(l1.getWorld(), (l1.x() + l2.x()) / 2, 1, (l1.z() + l2.z()) / 2);
                l3.setDirection(l2.toVector().subtract(l1.toVector()));
                dxz *= 0.5;
                pd.getMarker().teleport(l3.add(dxz, 0, dxz));
                return true;
            }
        } else if (start instanceof Road r) {
            //在桥上
            if (r.hasNode(end)) {
                r.players.remove(p);
                moveTo(p, end);
                var l = GameMap.getLocation((Island) end).add(dxz, 0, dxz);
                pd.getMarker().teleport(l);
                return true;
            }
        }
        return false;
    }

    public void moveTo(Player p, Node end) {
        PlayerData pd = players_data.get(p);
        pd.location = end;
        var is = pd.getActions();
        for (int i = 0; i < is.size(); i++) {
            p.getInventory().setItem(i, is.get(i));
        }
        if (end instanceof Resource r) {
            r.giveMoney(p);
        }
        end.players.add(p);
        pd.addAction(-1);
    }

    public void breakRoad(Player p, Road r) {
        p.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(Component.text("         %s破坏了一座桥;".formatted(p.getName()))));
        r.setMaterial(Material.AIR);
        r.players.forEach(player -> {
            PlayerData pd = players_data.get(player);
            var wool = pd.items.stream().filter(item -> item instanceof Wool).findFirst();
            if (wool.isPresent()) {
                Bukkit.broadcast(Component.text("          %s使用羊毛防止掉落虚空;".formatted(player.getName())));
                r.setMaterial(Material.WHITE_WOOL);
                pd.items.remove(wool.get());
                players_data.get(p).resetInventoryItems();
            } else intoVoid(p, player);
        });
    }
}