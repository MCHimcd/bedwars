package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;

public class IronAxe extends Card {
    @Override
    public void effect() {
    }
    @Override
    public int power() {
        return 2;
    }
    @Override
    public int costMoney() {
        return 4;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10005;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}