package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.players_data;

public class PotionOfSwiftness extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        player.getWorld().sendMessage(Component.text("           §l%s使用了 §b迅捷药水".formatted(player.getName())));
        PlayerData playerData = players_data.get(player);
        playerData.addAction(1);
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
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 20006;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<red>迅捷药水");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>迅捷:使用后本回合多一个行动力",
                "<white>使用此道具不消耗行动点",
                "",
                "<aqua>数量上限:1",
                "<green>经济:10"
        ));
    }

    @Override
    public boolean CanUse() {
        return true;
    }
}