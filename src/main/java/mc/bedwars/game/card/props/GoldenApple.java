package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.players_data;

public class GoldenApple extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        PlayerData playerData = players_data.get(player);
        player.getWorld().sendMessage(Component.text("           §l%s使用了 §6金苹果".formatted(player.getName())));
        if (playerData.getPower() < playerData.getMaxPower()) {
            playerData.setHealth(100);
        } else playerData.addTemporaryPower(2);
        return true;
    }

    @Override
    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 6;
    }

    @Override
    public int itemMaxCount() {
        return 999;
    }

    @Override
    public int CustomModelData() {
        return 20007;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<red>金苹果");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>恢复:如当前血量没有达到上限",
                "<white>则恢复满血量",
                "<white>强化:如当前血量已经达到上限",
                "<white>则使当前战力值+2",

                "",
                "<aqua>每次购买数量:1",
                "<green>经济:6"
        ));
    }

    @Override
    public boolean CanUse() {
        return true;
    }
}