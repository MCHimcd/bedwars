package mc.bedwars.game.card.equips;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class IronAxe extends Card implements Tool, Equip {
    @Override
    public boolean effect(Player player) {
        return true;

    }

    @Override
    public int power() {
        return 2;
    }

    @Override
    public int costMoney() {
        return 4;
    }

    @Override
    public int itemMaxCount() {
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 10005;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<aqua>斧");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>拆除:可以拆除与自己当前所在同",
                "<white>一岛屿材质包为 木板 的桥或材质为",
                "<white>木板 防爆玻璃 的方块",

                "",
                "<aqua>每次购买数量:1",
                "<green>经济:4"
        ));
    }

    @Override
    public boolean CanUse() {
        return false;
    }
}