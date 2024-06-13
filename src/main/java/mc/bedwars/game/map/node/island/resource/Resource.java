package mc.bedwars.game.map.node.island.resource;

import mc.bedwars.game.map.node.island.Island;

public abstract class Resource extends Island {
    private int r_amount;
    private int g_cd = getGenerationCD();

    public Resource(int x, int y) {
        super(x, y);
    }

    abstract int getMaxAmount();

    abstract int getMoneyOfEach();

    abstract int getGenerationCD();

    public int getMoney() {
        return r_amount * getMoneyOfEach();
    }

    public void generate() {
        if (--g_cd == 0) {
            r_amount = Math.min(r_amount + 1, getMaxAmount());
        }
        g_cd = getGenerationCD();
    }
}