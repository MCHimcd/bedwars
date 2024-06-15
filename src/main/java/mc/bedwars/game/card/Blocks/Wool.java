package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Wool extends Card implements isBlock{
    @Override
    public boolean effect(Player player) {
        return true;
    }
    public Material material(){
        return Material.WHITE_WOOL;
    }

    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 8;
    }
    @Override
    public int itemMaxCount() {
        return 4;
    }
    @Override
    public int CustomModelData() {
        return 40003;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("羊毛");
    }
    @Override
    public boolean CanUse(){
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("守床，搭路");
    }
}