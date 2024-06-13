package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import org.bukkit.entity.Player;

public class EnderPearl extends Card {
    @Override
    public void effect(Player player) {

    }
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 16;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 20003;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}