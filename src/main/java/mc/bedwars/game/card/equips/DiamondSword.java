package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;

public class DiamondSword extends Card {
    public void effect() {
    }
    @Override
    public int power() {
        return 4;
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
        return 10006;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}