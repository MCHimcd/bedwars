package mc.bedwars.game.map.node;

public class Emerald extends Node{
    public Emerald(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "emerald";
    }
}