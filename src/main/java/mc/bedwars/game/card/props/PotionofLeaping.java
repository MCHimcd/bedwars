package mc.bedwars.game.card.props;

import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.players_data;

public class PotionofLeaping extends Card implements Duration,isProps{
    @Override
    public void effect(Player player) {
        PlayerData playerData = players_data.get(player);
        playerData.addDpower(2);
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §9跳跃药水".formatted(player.getName())));

    }
    public boolean isProp(){
        return true;
    };
    @Override
    public int DurationRound() {
        return 1;
    }
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
        return 20005;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("跳跃药水");
    }
    @Override
    public boolean CanUse(){
        return true;
    }
    @Override
    public Component Introduction() {
        return Component.text("获得两点临时战力;");
    }
}