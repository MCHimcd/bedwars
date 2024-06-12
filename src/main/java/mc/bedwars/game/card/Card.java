package mc.bedwars.game.card;

public abstract class Card {
    abstract public void effect();
    abstract public int power();
    abstract public int costMoney();
    abstract public int itemMaxCount();
    abstract public int CustomModelData();
    abstract public boolean CanDrop();
    //1xxxx   装备
    //2xxxx   道具
    //3xxxx   加成
    //4xxxx   方块
}