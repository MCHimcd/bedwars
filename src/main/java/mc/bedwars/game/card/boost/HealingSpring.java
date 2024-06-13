package mc.bedwars.game.card.boost;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class HealingSpring extends Card {
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
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 30003;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
    @Override
    public Component Name() {
        return Component.text("治疗泉");
    }
    @Override
    public boolean CanUse(){
        return false;
    }

    @Override
    public Component Introduction() {
        return Component.text("当玩家在己方床位置时增加3点战力");
    }
}