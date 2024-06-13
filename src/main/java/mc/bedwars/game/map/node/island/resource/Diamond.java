package mc.bedwars.game.map.node.island.resource;

public class Diamond extends Resource{
    public Diamond(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "diamond";
    }

    @Override
    public int getMaxAmount() {
        return 2;
    }

    @Override
    public int getMoneyOfEach() {
        return 8;
    }

    @Override
    int getGenerationCD() {
        return 2;
    }
}