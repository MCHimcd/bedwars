package mc.bedwars.game.card.boost;

import mc.bedwars.game.card.Card;

public class HealingSpring extends Card {
    public void effect() {
        //当玩家在基地时获得3战力
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
        return 30003;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
}