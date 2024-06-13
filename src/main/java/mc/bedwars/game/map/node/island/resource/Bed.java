package mc.bedwars.game.map.node.island.resource;

public class Bed extends Resource{
    public Bed(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "bed";
    }

    @Override
    public int getMaxAmount() {
        return 4;
    }

    @Override
    public int getMoneyOfEach() {
        return 2;
    }

    @Override
    int getGenerationCD() {
        return 1;
    }
}