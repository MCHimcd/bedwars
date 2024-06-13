package mc.bedwars.game.map;

import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.game.map.node.island.Grass;
import mc.bedwars.game.map.node.island.resource.*;
import mc.bedwars.game.player.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static mc.bedwars.game.GameState.nextPlayer;
import static mc.bedwars.game.GameState.players_data;

//游戏地图
public class GameMap {
    public final List<Road> roads = new ArrayList<>();
    /*
    金。绿。金
    。钻草钻。
    蓝草绿草红
    。钻草钻。
    金。黄。金
    */
    public List<Node> nodes = new ArrayList<>(List.of(
            new Gold(0, 0),
            new Gold(0, 4),
            new Gold(4, 0),
            new Gold(4, 4),
            new Bed(0, 2),
            new Bed(2, 0),
            new Bed(2, 4),
            new Bed(4, 2),
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

    public void buildRoad(Node start, Node end, Material material) {
        if ((start instanceof Bed && end instanceof Gold) || (start instanceof Gold && end instanceof Bed)) {
            var r = roads.stream().filter(road -> road.hasNode(start)).findFirst();
            if (r.isPresent()) {
                roads.add(new Road(r.get(), end, material));
            } else {
                var r1 = new Road(start, end, material);
                var r2 = new Road(r1, end, Material.AIR);
                r1.setEnd(r2);
                roads.add(r1);
                roads.add(r2);
            }
        } else {
            roads.add(new Road(start, end, material));
        }
    }

    public boolean move(Player p, Node start, Node end) {
        PlayerData pd = players_data.get(p);
        var road = roads.stream().filter(r -> r.getMaterial() != Material.AIR && r.hasNode(start) && r.hasNode(end)).findFirst();
        if (road.isPresent() || start.equals(end)) {
            pd.location = end;
            if (end instanceof Resource r) {
                r.giveMoney(pd);
            }
            nextPlayer();
            return true;
        }
        return false;
    }
}