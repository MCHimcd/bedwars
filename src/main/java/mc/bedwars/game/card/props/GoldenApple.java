package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;

public class GoldenApple extends Card {
    public void effect() {
        //如果当前战力没达到上限，可以回复到战力上限(消耗行动点;如果战力到达上限，则使战力+2  (不需要行动点
        //看这个卡来说好像还有一种设定 就是战力不是一定会达到上限的，那这样这也太难算了吧 能看到不，打个字
    }
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 6;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 20007;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}