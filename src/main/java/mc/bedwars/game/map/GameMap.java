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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.bedwars.game.GameState.players_data;

//游戏地图
public class GameMap {
    public final Map<Marker, Node> markers = new HashMap<>();
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
            new Platform(0,1),
            new Platform(0,3),
            new Platform(1,0),
            new Platform(3,0),
            new Platform(1,4),
            new Platform(3,4),
            new Platform(4,1),
            new Platform(4,3),
            new Emerald(2, 2)
    ));

    public GameMap(World world) {
        //生成markers
        for (var i : islands) {
            markers.put(world.spawn(getLocation(i), Marker.class), i);
        }
    }

    public static void intoVoid(Player causer, Player p) {
        var pd = players_data.get(p);
        var ep = pd.items.stream().filter(item -> item instanceof EnderPearl).findFirst();
        if (ep.isPresent()) {
            ((EnderPearl)ep.get()).backHome(p);
        } else pd.die(List.of(causer));
    }

    public static Location getLocation(Island island) {
        return new Location(Bukkit.getWorld("world"), (island.getX() - 2) * 20, 1, (island.getY() - 2) * 20);
    }

    public void buildRoad(Island start, Island end, Material material) {
        if ((start instanceof Bed && end instanceof Gold) || (start instanceof Gold && end instanceof Bed)) {
            var r = roads.stream().filter(road -> road.hasNode(start)).findFirst();
            if (r.isPresent()) {
                roads.add(new Road(material, r.get(), end));
            } else {
                var r1 = new Road(material, start, end);
                var r2 = new Road(Material.AIR, r1, end);
                r1.setEnd(r2);
                roads.add(r1);
                roads.add(r2);
            }
        } else if (
                (start instanceof Grass && end instanceof Grass)
                        || (start instanceof Diamond && end instanceof Emerald)
                        || (start instanceof Emerald && end instanceof Diamond)
        ) {
            roads.add(new Road(material, start, end, getIsland(2 * start.getX() - end.getX(), 2 * start.getY() - end.getY())));
        } else {
            roads.add(new Road(material, start, end));
        }
    }

    public boolean move(Player p, Node start, Node end) {
        PlayerData pd = players_data.get(p);
        if (start.equals(end)) {
            start.players.remove(p);
            moveTo(p, end);//获得钱
            return true;
        } else if (pd.location instanceof Island island) {
            //在岛上
            var road = roads.stream().filter(r -> r.getMaterial() != Material.AIR && r.hasNode(start) && r.hasNode(end)).findFirst();
            if (road.isPresent()) {
                island.players.remove(p);
                moveTo(p, road.get());
                var l1 = GameMap.getLocation(island);
                var l2 = GameMap.getLocation((Island) end);
                var l3 = new Location(l1.getWorld(), (l1.x() + l2.x()) / 2, 1, (l1.z() + l2.z()) / 2);
                pd.getMarker().teleport(l3);
                return true;
            }
        } else if (start instanceof Road r) {
            //在桥上
            if (r.hasNode(end)){
                r.players.remove(p);
                moveTo(p, end);
                var l = GameMap.getLocation((Island) end);
                pd.getMarker().teleport(l);
                return true;
            }
        }
        return false;
    }

    private void moveTo(Player p, Node end) {
        PlayerData pd = players_data.get(p);
        pd.location = end;
        if (end instanceof Resource r) {
            r.giveMoney(p);
        }
        end.players.add(p);
        pd.addAction(-1);
    }

    public void breakRoad(Player p, Road r) {
        r.setMaterial(Material.AIR);
        r.players.forEach(player -> {
            PlayerData pd = players_data.get(player);
            var wool = pd.items.stream().filter(item -> item instanceof Wool).findFirst();
            if (wool.isPresent()) {
                r.setMaterial(Material.WHITE_WOOL);
                pd.items.remove(wool.get());
            } else intoVoid(p, player);
        });
        p.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(Component.text("<S>     %s破坏了一座桥;".formatted(p.getName()))));
    }

    private Island getIsland(int x, int y) {
        var i = islands.stream().filter(island -> island.getX() == x && island.getY() == y).findFirst();
        return i.orElse(null);
    }
}