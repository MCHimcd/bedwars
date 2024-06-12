package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class Tnt extends Card {
    public void effect() {
        //破坏
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
        return 20001;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}