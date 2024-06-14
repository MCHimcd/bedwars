package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class EnderPearl extends Card implements isProps{
    @Override
    public void effect(Player player) {
        //todo 使用珍珠
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §7末影之眼".formatted(player.getName())));
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
        return 20003;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("末影珍珠");
    }
    @Override
    public boolean CanUse(){
        return true;
    }
    @Override
    public Component Introduction() {
        return Component.text("当被打入虚空时可以使用，回到相邻岛屿");
    }
}