package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;
import org.bukkit.entity.Player;

public class DiamondEquips extends Card {
    @Override
    public void effect(Player player) {

    }
    @Override
    public int power() {
        return 8;
    }
    @Override
    public int costMoney() {
        return 32;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10007;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
}