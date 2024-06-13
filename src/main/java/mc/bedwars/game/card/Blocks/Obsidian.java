package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Obsidian extends Card implements BlockCount{
    @Override
    public void effect(Player player) {

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
        return 2;
    }
    @Override
    public int CustomModelData() {
        return 40005;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("黑耀石");
    }
    @Override
    public boolean CanUse(){
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("保护己方的床");
    }
}