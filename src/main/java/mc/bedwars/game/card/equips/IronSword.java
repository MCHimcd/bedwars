package mc.bedwars.game.card.equips;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class IronSword extends Card implements Equip {
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
        return 4;
    }

    @Override
    public int itemMaxCount() {
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 10001;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<aqua>铁剑");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>攻击:常驻提供2点战力值",
                "",
                "<aqua>每次购买数量:1",
                "<green>经济:4"
        ));
    }

    @Override
    public boolean CanUse() {
        return false;
    }
}