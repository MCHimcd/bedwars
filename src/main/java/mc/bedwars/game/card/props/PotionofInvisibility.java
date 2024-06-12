package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class PotionofInvisibility extends Card implements Duration{
    public void effect() {
        //战力+2 行动值+1  不消耗行动点
    }
    @Override
    public int DurationRound() {
        return 1;
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
        return 20008;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}