package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.players_data;

public class KbtStick extends Card implements Prop, NeedTarget {
    @Override
    public boolean effect(Player player) {
        var pd = players_data.get(player);
        var target = pd.getTarget();
        GameMap.intoVoid(player, target);
        player.getWorld().sendMessage(Component.text("          §l%s使用了 §1击退棒".formatted(player.getName())));
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
                "<white>可抛一枚骰子,如点数为6/5/4/3",
                "<white>则将对方击退到上/下/左/右方岛屿",
                "<white>,如该方向没有岛屿或没有桥,则视为",
                "<white>击落虚空,1/2此卡无效,照常消耗",

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