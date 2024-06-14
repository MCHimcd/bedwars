package mc.bedwars.game.card.props;

import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.players_data;

public class GoldenApple extends Card implements isProps{
    @Override
    public void effect(Player player) {
        PlayerData playerData = players_data.get(player);
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §6金苹果".formatted(player.getName())));
        if (playerData.getPower()<playerData.getMaxPower()){
            playerData.setHealth(100);
        }else playerData.addDpower(2);
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
    @Override
    public Component Name() {
        return Component.text("金苹果");
    }
    @Override
    public boolean CanUse(){
        return true;
    }
    @Override
    public Component Introduction() {
        return Component.text("当血量未满时将血量回满，当血量已满时增加2点临时战力。");
    }
}