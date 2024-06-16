package mc.bedwars.game.card;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;


public abstract class Card {
    abstract public boolean effect(Player player);

    abstract public int power();

    abstract public int costMoney();

    abstract public int itemMaxCount();

    abstract public int CustomModelData();

    abstract public boolean CanDrop();

    abstract public Component Name();

    abstract public Component Introduction();

    abstract public boolean CanUse();
//    abstract public boolean NeedTarget();

    //1xxxx   装备
    //2xxxx   道具
    //3xxxx   加成
    //4xxxx   方块
    //5xxxx   床
    //6xxxx   其他
}