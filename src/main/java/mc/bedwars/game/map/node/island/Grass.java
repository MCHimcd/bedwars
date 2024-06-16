package mc.bedwars.game.map.node.island;

public class Grass extends Island {
    public Grass(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "§2植物";
    }

}