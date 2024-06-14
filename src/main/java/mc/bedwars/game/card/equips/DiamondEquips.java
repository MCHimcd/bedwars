package mc.bedwars.game.card.equips;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class DiamondEquips extends Card implements Equip {
    @Override
    public void effect(Player player) {
    }

    @Override
    public int power() {
        return 8;
    }
    @Override
    public int costMoney() {
        return 32;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10007;
    }
    @Override
    public boolean CanDrop(){
        return false;
    }
    @Override
    public Component Name() {
        return Component.text("钻石套装");
    }
    @Override
    public boolean CanUse(){
        return false;
    }

    @Override
    public Component Introduction() {
        return Component.text("获得8点战力。");
    }
}