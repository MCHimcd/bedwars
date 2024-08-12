package mc.bedwars.game.card;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;


public abstract class Card implements Cloneable {
    abstract public boolean effect(Player player);

    abstract public int power();

    abstract public int costMoney();

    abstract public int itemMaxCount();

    abstract public int CustomModelData();

    abstract public boolean CanDrop();

    abstract public Component Name();

    abstract public List<Component> Lore();

    abstract public boolean CanUse();

    @Override
    public Card clone() {
        try {
            return (Card) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

//    abstract public boolean NeedTarget();

    //1xxxx   装备
    //2xxxx   道具
    //3xxxx   加成
    //4xxxx   方块
    //5xxxx   床
    //6xxxx   其他
}