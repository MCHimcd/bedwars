package mc.bedwars.game.card.Blocks;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class Plank extends Card implements isBlock, multiplePurchase {
    @Override
    public boolean effect(Player player) {
        return true;
    }

    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 12;
    }

    @Override
    public int itemMaxCount() {
        return 999;
    }

    @Override
    public int CustomModelData() {
        return 40001;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<white>木板");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>搭路:可用于岛屿之间的连接搭路",
                "<white>守床:可用于保护己方的床",
                "<white>可破坏:斧 火球 TNT",
                "",
                "<aqua>每次购买数量:4",
                "<green>经济:12"
        ));
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    public Material material() {
        return Material.CRIMSON_PLANKS;
    }

    @Override
    public int getPurchaseAmount() {
        return 4;
    }
}