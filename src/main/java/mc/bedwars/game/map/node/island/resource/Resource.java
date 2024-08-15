package mc.bedwars.game.map.node.island.resource;

import mc.bedwars.factory.Message;
import mc.bedwars.game.GameState;
import mc.bedwars.game.map.node.island.Island;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class Resource extends Island {
    private int r_amount = 0;
    private int g_cd = getGenerationCD();

    public Resource(int x, int y) {
        super(x, y);
    }

    public int getAmount() {
        return r_amount;
    }

    /**
     * 尝试给指定的玩家钱
     */
    public void giveMoney(Player p) {
        var pd = GameState.players_data.get(p);
        if (r_amount > 0) {
            pd.addMoney(r_amount * getMoneyOfEach());
            p.sendMessage(Message.rMsg("          获得 %d 钱".formatted(r_amount * getMoneyOfEach())));
            p.playSound(p, Sound.ENTITY_ITEM_PICKUP, 1f, 2f);
            r_amount = 0;
        }
    }

    abstract int getMoneyOfEach();

    public void generate() {
        if (--g_cd == 0) {
            r_amount = Math.min(r_amount + 1, getMaxAmount());
            g_cd = getGenerationCD();
        }
    }

    abstract int getMaxAmount();

    /**
     * @return 生成的间隔（轮）
     */
    abstract int getGenerationCD();
}