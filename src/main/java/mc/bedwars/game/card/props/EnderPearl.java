package mc.bedwars.game.card.props;

import mc.bedwars.game.GameState;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.resource.Bed;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.map;

public class EnderPearl extends Card implements Prop {
    @Override
    public void effect(Player player) {
        var pd = GameState.players_data.get(player);
        pd.addAction(1);
        pd.location = pd.target_location;
        map.move(player, pd.location, pd.target_location);
        player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §7末影之眼".formatted(player.getName())));

    }

    public void backHome(Player player){
        var pd = GameState.players_data.get(player);
        map.islands.stream().filter(island -> {
            return island instanceof Bed b && b.getOrder()==pd.getOrder();
        }).findFirst().ifPresent(island -> {
            pd.location=island;
            pd.getMarker().teleport(GameMap.getLocation(island));
        });
    }

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
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return Component.text("末影珍珠");
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("当被打入虚空时可以使用，回到相邻岛屿");
    }
}