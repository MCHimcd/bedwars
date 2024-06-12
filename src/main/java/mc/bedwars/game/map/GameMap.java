package mc.bedwars.game.map;

import mc.bedwars.game.map.node.*;

import java.util.ArrayList;
import java.util.List;

//游戏地图
public class GameMap {
    public List<Node> nodes = new ArrayList<>() {{
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                add(new Gold(i, j));
                add(new Diamond(i, j));
                add(new Grass(i, j));
                add(new Bed(i, j));
            }
        }
    }};
}