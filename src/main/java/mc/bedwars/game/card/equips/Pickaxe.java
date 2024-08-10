package mc.bedwars.game.card.equips;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class Pickaxe extends Card implements Tool, Equip {
    @Override
    public boolean effect(Player player) {
        return true;
    }

    @Override
    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 4;
    }

    @Override
    public int itemMaxCount() {
        return 2;
    }

    @Override
    public int CustomModelData() {
        return 10008;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<aqua>镐");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>拆除:可以拆除与自己当前所在同",
                "<white>一岛屿材质为 末地石 的桥或材质",
                "<white>为末地石 防爆玻璃 的方块或消耗",
                "<white>2个此道具拆除一个黑曜石方块",
                "",
                "<aqua>数量上限:1",
                "<green>经济:4"
        ));
    }

    @Override
    public boolean CanUse() {
        return false;
    }
}