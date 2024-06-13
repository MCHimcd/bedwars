package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Fireball extends Card {
    @Override
    public void effect(Player player) {

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
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 20004;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("火球");
    }
    @Override
    public boolean CanUse(){
        return true;
    }
    @Override
    public Component Introduction() {
        return Component.text("可以远距离破坏材质为木板或羊毛的桥。");
    }
}