package mc.bedwars.game.card.boost;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Protection extends Card{
    @Override
    public boolean effect(Player player) {
        return true;
    }
    @Override
    public int power() {
        return 1;
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
        return 30002;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
    @Override
    public Component Name() {
        return Component.text("保护");
    }
    @Override
    public boolean CanUse(){
        return false;
    }

    @Override
    public Component Introduction() {
        return Component.text("获得战力");
    }
}