package mc.bedwars.game.card.props;

import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.players_data;

public class GoldenApple extends Card {
    @Override
    public void effect(Player player) {
        PlayerData playerData = players_data.get(player);
        if (playerData.getPower()<playerData.getMaxPower()){
            playerData.setHealth(100);
        }else playerData.addDpower(2);
    }
    @Override
    public int power() {
        return 0;
    }
    @Override
    public int costMoney() {
        return 6;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 20007;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
}