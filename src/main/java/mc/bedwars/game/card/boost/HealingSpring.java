package mc.bedwars.game.card.boost;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class HealingSpring extends Card {
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
        return 16;
    }

    @Override
    public int itemMaxCount() {
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 30003;
    }

    @Override
    public boolean CanDrop() {
        return false;
    }

    @Override
    public Component Name() {
        return rMsg("<green>治愈泉");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>治愈:当玩家在自己基地时战力值+3",
                "",
                "<aqua>数量上限:1",
                "<green>经济:16"
        ));
    }

    @Override
    public boolean CanUse() {
        return false;
    }
}