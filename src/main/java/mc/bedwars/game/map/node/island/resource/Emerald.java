package mc.bedwars.game.map.node.island.resource;

public class Emerald extends Resource{
    public Emerald(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "绿宝石";
    }

    @Override
    int getMaxAmount() {
        return 2;
    }

    @Override
    int getMoneyOfEach() {
        return 16;
    }

    @Override
    int getGenerationCD() {
        return 3;
    }
}