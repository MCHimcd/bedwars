package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;

public class Scissors extends Card {
    public void effect() {
        //拆除羊毛 防爆玻璃
    }
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 2;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10004;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}