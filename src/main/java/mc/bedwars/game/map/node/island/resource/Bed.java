package mc.bedwars.game.map.node.island.resource;

public class Bed extends Resource {
    private final int order;

    public Bed(int x, int y, int order) {
        super(x, y);
        this.order = order;
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

    public int getOrder() {
        return order;
    }
}