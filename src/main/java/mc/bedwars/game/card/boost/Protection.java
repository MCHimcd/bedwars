package mc.bedwars.game.card.boost;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class Protection extends Card {
    @Override
    public boolean effect(Player player) {
        return true;
    }

    @Override
    public int power() {
        return 1;
    }

    @Override
    public int costMoney() {
        return 12;
    }

    @Override
    public int itemMaxCount() {
        return 4;
    }

    @Override
    public int CustomModelData() {
        return 30002;
    }

    @Override
    public boolean CanDrop() {
        return false;
    }

    @Override
    public Component Name() {
        return rMsg("<green>保护");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>保护I:使盔甲提供的战斗力+1",
                "<white>保护II:使盔甲提供的战斗力+2",
                "<white>保护III:使盔甲提供的战斗力+3",
                "<white>保护IV:使盔甲提供的战斗力+4",
                "",
                "<aqua>每次购买数量:4",
                "<green>经济:12"
        ));
    }

    @Override
    public boolean CanUse() {
        return false;
    }
}