package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import static mc.bedwars.game.GameState.players_data;

public class KbtStick extends Card implements Prop, NeedTarget {
    @Override
    public void effect(Player player) {
        var pd=players_data.get(player);
        var target=pd.getTarget();
        GameMap.intoVoid(player,target);
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §1击退棒".formatted(player.getName())));
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