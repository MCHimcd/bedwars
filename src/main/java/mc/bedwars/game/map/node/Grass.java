package mc.bedwars.game.map.node;

public class Grass extends Node{
    public Grass(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "grass";
    }
}