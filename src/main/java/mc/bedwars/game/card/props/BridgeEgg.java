package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class BridgeEgg extends Card {
    public void effect() {
        //直接搭到岛对面
    }
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 12;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 20002;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}