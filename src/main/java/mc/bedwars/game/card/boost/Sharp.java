package mc.bedwars.game.card.boost;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class Sharp extends Card {
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
        return 8;
    }

    @Override
    public int itemMaxCount() {
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 30001;
    }

    @Override
    public boolean CanDrop() {
        return false;
    }

    @Override
    public Component Name() {
        return rMsg("<green>锋利");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>锋利:使武器提供的战斗力+2",
                "",
                "<aqua>每次购买数量:1",
                "<green>经济:8"
        ));
    }

    @Override
    public boolean CanUse() {
        return false;
    }
}