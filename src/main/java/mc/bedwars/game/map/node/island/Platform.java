package mc.bedwars.game.map.node.island;

public class Platform extends Island{
    public Platform(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "§5平台";
    }

}
