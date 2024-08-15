package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.players_data;

public class PotionOfInvisibility extends Card implements Duration, Prop {
    @Override
    public boolean effect(Player player) {
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §b隐身药水".formatted(player.getName())));
        PlayerData playerData = players_data.get(player);
        playerData.addTemporaryPower(2);
        playerData.addAction(1);
        return true;
    }

    @Override
    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 20;
    }

    @Override
    public int itemMaxCount() {
        return 999;
    }

    @Override
    public int CustomModelData() {
        return 20008;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<red>隐身药水");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>隐身:使用后本轮此战斗力值+2",
                "<white>且行动值+1",
                "<white>使用此道具不消耗行动点",
                "",
                "<aqua>每次购买数量:1",
                "<green>经济:20"
        ));
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    @Override
    public int DurationRound() {
        return 1;
    }
}