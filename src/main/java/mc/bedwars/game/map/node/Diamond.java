package mc.bedwars.game.map.node;

public class Diamond extends Node{
    public Diamond(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "diamond";
    }
}