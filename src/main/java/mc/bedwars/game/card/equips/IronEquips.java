package mc.bedwars.game.card.equips;
import mc.bedwars.game.card.Card;
import org.bukkit.entity.Player;

public class IronEquips extends Card {
    @Override
    public void effect(Player player) {

    }
    @Override
    public int power() {
        return 4;
    }
    @Override
    public int costMoney() {
        return 12;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10002;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
}