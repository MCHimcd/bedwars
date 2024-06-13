package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ExplosionproofGlass extends Card implements isBlock{
    @Override
    public void effect(Player player) {

    }
    public boolean isBlock(){
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
        return 4;
    }
    @Override
    public int CustomModelData() {
        return 40004;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("防爆玻璃");
    }
    @Override
    public boolean CanUse(){
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("方块,可以防爆");
    }
}