package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Plank extends Card implements BlockCount,isBlock{
    @Override
    public void effect(Player player) {

    }
    public boolean isBlock(){
        return true;
    }
    @Override
    public int getCount(){
        return Count;
    };
    public int Count = itemMaxCount();
    @Override
    public int power() {
        return 0;
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
        return 40001;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("木板");
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