package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;
import org.bukkit.entity.Player;

public class ExplosionproofGlass extends Card implements BlockCount{
    @Override
    public void effect(Player player) {

    }
    @Override
    public int getCount(){
        return Count;
    };
    public int Count = itemMaxCount();
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
        return 4;
    }
    @Override
    public int CustomModelData() {
        return 40004;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}