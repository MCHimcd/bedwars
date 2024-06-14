package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import mc.bedwars.menu.ChoosePlayerMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class KbtStick extends Card implements isProps, needTarget {
    @Override
    public void effect(Player player) {
        //todo 使用击退棒 等将掉入虚空设成布尔值后再写
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §1击退棒".formatted(player.getName())));
    }
    public boolean isProp(){
        return true;
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
        return 10003;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("击退棒");
    }
    @Override
    public boolean CanUse(){
        return true;
    }
    @Override
    public Component Introduction() {
        return Component.text("击飞敌人。");
    }

}