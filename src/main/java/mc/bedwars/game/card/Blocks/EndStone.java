package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
public class EndStone extends Card implements isBlock{
    public boolean isBlock(){
        return true;
    }
    public Material material(){
        return Material.END_STONE;
    }

    @Override
    public void effect(Player player) {

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
        return 4;
    }
    @Override
    public int CustomModelData() {
        return 40002;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public boolean CanUse(){
        return true;
    }

    @Override
    public Component Name() {
        return Component.text("末地石");
    }

    @Override
    public Component Introduction() {
        return Component.text("守床，搭路");
    }
}