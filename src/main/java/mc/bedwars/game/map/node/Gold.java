package mc.bedwars.game.map.node;

public class Gold extends Node{
    public Gold(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "gold";
    }
}