package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.players_data;

public class PotionOfLeaping extends Card implements Duration, Prop {
    @Override
    public boolean effect(Player player) {
        PlayerData playerData = players_data.get(player);
        playerData.addTemporaryPower(2);
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §9跳跃药水".formatted(player.getName())));
        return true;
    }

    @Override
    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 10;
    }

    @Override
    public int itemMaxCount() {
        return 999;
    }

    @Override
    public int CustomModelData() {
        return 20005;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<red>跳跃药水");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>跳跃提升:使用后本轮次战力值",
                "<white>+2",
                "<white>使用此道具不消耗行动点",
                "",
                "<aqua>每次购买数量:1",
                "<green>经济:10"
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