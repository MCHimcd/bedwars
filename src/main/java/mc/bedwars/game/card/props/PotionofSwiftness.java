package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class PotionofSwiftness extends Card {
    public void effect() {
        //+一个行动点
    }
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 10;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 20006;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}