package mc.bedwars.game.map;

import mc.bedwars.game.card.props.EnderPearl;
import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.game.map.node.island.Grass;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.game.map.node.island.resource.*;
import mc.bedwars.game.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.bedwars.game.GameState.nextPlayer;
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
            new Emerald(2, 2)
    ));

    public GameMap(World world) {
        //生成markers
        for (var i : islands) {
            markers.put(world.spawn(new Location(world, (i.getX() - 2) * 20, 0, (i.getY() - 2) * 20), Marker.class), i);
        }
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
        var road = roads.stream().filter(r -> r.getMaterial() != Material.AIR && r.hasNode(start) && r.hasNode(end)).findFirst();
        if (road.isPresent() || start.equals(end)) {
            if (start instanceof Island i1) i1.players.remove(p);
            if (end instanceof Island i2) i2.players.add(p);
            pd.location = end;
            if (end instanceof Resource r) {
                r.giveMoney(pd);
            }
            nextPlayer();
            return true;
        }
        return false;
    }

    public void breakRoad(Player p, Road r) {
        r.setMaterial(Material.AIR);
        r.players.forEach(player -> {
            PlayerData pd = players_data.get(player);
            var ep = pd.items.stream().filter(item -> item instanceof EnderPearl).findFirst();
            if (ep.isPresent()) ep.get().effect(player);
            else pd.die(List.of(p));
        });
    }

    private Island getIsland(int x, int y) {
        var i = islands.stream().filter(island -> island.getX() == x && island.getY() == y).findFirst();
        return i.orElse(null);
    }
}