package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class PotionofLeaping extends Card implements Duration{
    public void effect() {
        //提升本轮2点战力  不消耗行动点
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
        return 20005;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}