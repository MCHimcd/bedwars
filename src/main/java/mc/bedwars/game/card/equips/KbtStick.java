package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;

public class KbtStick extends Card {
    public void effect() {
        //击退玩家
    }
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
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10003;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}