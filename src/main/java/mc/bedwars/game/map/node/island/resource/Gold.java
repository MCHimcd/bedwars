package mc.bedwars.game.map.node.island.resource;

public class Gold extends Resource{
    public Gold(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "金";
    }

    @Override
    int getMaxAmount() {
        return 2;
    }

    @Override
    int getMoneyOfEach() {
        return 4;
    }

    @Override
    int getGenerationCD() {
        return 1;
    }
}