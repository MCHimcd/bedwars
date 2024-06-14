package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Tnt extends Card implements isProps{
    @Override
    public void effect(Player player) {
        //todo 炸桥
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §cTNT".formatted(player.getName())));

    }
    public boolean isProp(){
        return true;
    };
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
        return 20001;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("TNT");
    }
    @Override
    public boolean CanUse(){
        return true;
    }
    @Override
    public Component Introduction() {
        return Component.text("破坏除黑曜石和防爆玻璃以外的所有方块,或以上材质的岛屿");
    }
}