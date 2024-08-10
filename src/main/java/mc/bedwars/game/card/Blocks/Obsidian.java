package mc.bedwars.game.card.Blocks;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class Obsidian extends Card implements isBlock {
    @Override
    public boolean effect(Player player) {
        return true;
    }

    public Material material() {
        return Material.OBSIDIAN;
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
        return 2;
    }

    @Override
    public int CustomModelData() {
        return 40005;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<white>黑曜石");
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>守床:可用于保护己方的床",
                "<white>可破坏:镐*2",
                "",
                "<aqua>数量上限:2",
                "<green>经济:12"
        ));
    }
}

