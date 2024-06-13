package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class IronAxe extends Card {
    @Override
    public void effect(Player player) {

    }
    @Override
    public int power() {
        return 2;
    }
    @Override
    public int costMoney() {
        return 4;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10005;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("铁斧");
    }
    @Override
    public boolean CanUse(){
        return false;
    }
    @Override
    public Component Introduction() {
        return Component.text("破坏木板或木板材质的桥。");
    }
}