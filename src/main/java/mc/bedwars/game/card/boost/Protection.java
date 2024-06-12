package mc.bedwars.game.card.boost;

import mc.bedwars.game.card.Card;

public class Protection extends Card implements Boostlevel{
    public void effect() {
        if (getLevel()<4)Level++;
    }
    @Override
    public int Maxlevel() {
        return 4;
    }
    public int getLevel() {
        return Level;
    }
    public int Level=1;
    @Override
    public int power() {
        return Level;
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
        return 30002;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
}