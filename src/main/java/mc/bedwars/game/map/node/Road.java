package mc.bedwars.game.map.node;

public class Road extends Node{
    public Road(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "road";
    }
}