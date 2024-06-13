package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Scissors extends Card {
    @Override
    public void effect(Player player) {

    }
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 2;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10004;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("剪刀");
    }
    @Override
    public boolean CanUse(){
        return false;
    }
    @Override
    public Component Introduction() {
        return Component.text("破坏羊毛.");
    }
}