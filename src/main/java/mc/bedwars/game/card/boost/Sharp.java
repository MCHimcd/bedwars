package mc.bedwars.game.card.boost;

import mc.bedwars.game.card.Card;
import org.bukkit.entity.Player;

public class Sharp extends Card implements Boostlevel{
    @Override
    public void effect(Player player) {

    }
    @Override
    public int Maxlevel() {
        return 4;
    }
    public int getLevel() {
        return Level;
    }
    public int Level=1;
    @Override
    public int power() {
        return 1+Level;
    }
    @Override
    public int costMoney() {
        return 8;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 30001;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
}