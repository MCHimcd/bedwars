package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;

public class Pickaxe extends Card {
    public void effect() {
        //拆除末地石，或消耗两个该道具拆除黑曜石
    }
    @Override
    public int power() {
        return 0;
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
        return 10008;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}