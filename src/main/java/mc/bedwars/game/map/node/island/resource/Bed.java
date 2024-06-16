package mc.bedwars.game.map.node.island.resource;

public class Bed extends Resource {
    private final int order;

    public Bed(int x, int y, int order) {
        super(x, y);
        this.order = order;
    }

    @Override
    public String getType() {
        return "§%s%s队床".formatted(
                switch (order){
                    case 1->"c";
                    case 2->"a";
                    case 3->"9";
                    case 4->"e";
                    default -> "f";
                },
                switch (order){
                    case 1->"红";
                    case 2->"绿";
                    case 3->"蓝";
                    case 4->"黄";
                    default -> "null";
                }
        );
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