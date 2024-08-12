package mc.bedwars.game.card.equips;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class IronEquips extends Card implements Equip {
    @Override
    public boolean effect(Player player) {
        return true;
    }

    @Override
    public int power() {
        return 4;
    }

    @Override
    public int costMoney() {
        return 12;
    }

    @Override
    public int itemMaxCount() {
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 10002;
    }

    @Override
    public boolean CanDrop() {
        return false;
    }

    @Override
    public Component Name() {
        return rMsg("<aqua>铁装备");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>保护:常驻提供4点战力值",
                "<white>不掉落:该物品死亡不掉落",
                "",
                "<aqua>每次购买数量:1",
                "<green>经济:12"
        ));
    }

    @Override
    public boolean CanUse() {
        return false;
    }
}