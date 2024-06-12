package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class Fireball extends Card {
    public void effect() {
        //远程破坏羊毛木板
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
        return 20004;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}