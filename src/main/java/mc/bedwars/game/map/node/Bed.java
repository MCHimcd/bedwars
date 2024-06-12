package mc.bedwars.game.map.node;

public class Bed extends Node{
    public Bed(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "bed";
    }
}