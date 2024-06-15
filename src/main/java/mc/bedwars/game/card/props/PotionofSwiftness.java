package mc.bedwars.game.card.props;

import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.players_data;

public class PotionofSwiftness extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §b迅捷药水".formatted(player.getName())));
        PlayerData playerData = players_data.get(player);
        playerData.addAction(1);
        return true;
    }

    ;
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 10;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 20006;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("迅捷药水");
    }
    @Override
    public boolean CanUse(){
        return true;
    }
    @Override
    public Component Introduction() {
        return Component.text("立即获得1点行动值");
    }
}