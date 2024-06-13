package mc.bedwars.game.map;

import mc.bedwars.game.map.node.*;
import mc.bedwars.game.map.node.island.Grass;
import mc.bedwars.game.map.node.island.resource.*;

import java.util.ArrayList;
import java.util.List;

//游戏地图
public class GameMap {
    public List<Node> nodes = new ArrayList<>(List.of(
            new Gold(0,0),
            new Gold(0,4),
            new Gold(4,0),
            new Gold(4,4),
            new Bed(0,2),
            new Bed(2,0),
            new Bed(2,4),
            new Bed(4,2),
            new Diamond(1,1),
            new Diamond(3,3),
            new Diamond(3,1),
            new Diamond(1,3),
            new Grass(2,1),
            new Grass(1,2),
            new Grass(3,2),
            new Grass(2,3),
            new Emerald(2,2)
    ));
}