package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class EnderPearl extends Card {
    public void effect() {
        //瞬移自救
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
        return 20003;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}