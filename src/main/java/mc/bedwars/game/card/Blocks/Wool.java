package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;

public class Wool extends Card implements BlockCount{
    public void effect() {
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
        return 8;
    }
    @Override
    public int itemMaxCount() {
        return 4;
    }
    @Override
    public int CustomModelData() {
        return 40003;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}