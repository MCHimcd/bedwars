package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.game.map.node.island.Island;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.players_data;

public class KbtStick extends Card implements Prop, NeedTarget {
    @Override
    public boolean effect(Player player) {
        var pd = players_data.get(player);
        var target = pd.getTarget();
        player.getWorld().sendMessage(Component.text("          §l%s使用了 §1击退棒".formatted(player.getName())));

        var point = new Random().nextDouble();
        if ((pd.location instanceof Road && point > 2.0 / 3)
                || (pd.location instanceof Island && point > 0.5))
            GameMap.intoVoid(player, target);
        else player.sendMessage(rMsg("  §l击退棒未命中"));

        return true;
    }

    @Override
    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 8;
    }

    @Override
    public int itemMaxCount() {
        return 999;
    }

    @Override
    public int CustomModelData() {
        return 10003;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<red>击退棒");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>击退:当与另一玩家同处一个岛屿上时,",
                "<white>可抛一枚骰子,如点数大于3",
                "<white>则将对方击退到虚空",
                "<white>当与另一玩家同处一个桥上时",
                "<white>可抛一枚骰子,如点数大于2",
                "<white>则将对方击退到虚空",

                "",
                "<aqua>每次购买数量:1",
                "<green>经济:8"
        ));
    }

    @Override
    public boolean CanUse() {
        return true;
    }
}