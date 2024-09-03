package mc.bedwars.game.card;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;


public abstract class Card implements Cloneable {
    abstract public boolean effect(Player player);

    /**
     * @return 提供的力量
     */
    abstract public int power();

    /**
     * @return 购买时消耗的钱
     */
    abstract public int costMoney();

    /**
     * @return 玩家拥有此物品的最大数量
     */
    abstract public int itemMaxCount();

    abstract public int CustomModelData();

    /**
     * @return 死亡时是否掉落
     */
    abstract public boolean CanDrop();

    abstract public Component Name();

    abstract public List<Component> Lore();

    /**
     * @return 是否可以主动使用
     */
    abstract public boolean CanUse();

//    abstract public boolean NeedTarget();

    //1xxxx   装备
    //2xxxx   道具
    //3xxxx   加成
    //4xxxx   方块
    //5xxxx   床
    //6xxxx   其他
}