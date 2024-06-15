package mc.bedwars.game.card.boost;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Sharp extends Card{
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
    public boolean CanDrop(){
        return false;
    }
    @Override
    public Component Name() {
        return Component.text("锋利");
    }
    @Override
    public boolean CanUse(){
        return false;
    }

    @Override
    public Component Introduction() {
        return Component.text("获得2点战力");
    }
}