package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.node.island.Island;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.map;
import static mc.bedwars.game.GameState.players_data;

public class Fireball extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        var pd = players_data.get(player);
        if (pd.target_location_1 == null) {
            pd.target_location_1 = pd.target_location;
            player.sendMessage("下一个");
            return false;
        } else {
            Island i1 = (Island) pd.target_location_1;
            Island i2 = (Island) pd.target_location;
            map.roads.stream().filter(road -> road.hasNode(pd.location)).findFirst().ifPresent(
                    road -> {
                        if (switch (road.getMaterial()) {
                            case WHITE_WOOL, CRIMSON_PLANKS -> true;
                            default -> false;
                        }) map.breakRoad(player, road);
                    }
            );
            player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §1火球".formatted(player.getName())));
            pd.target_location_1 = null;
            pd.addAction(-1);
            return true;
        }
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
        return 20004;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return Component.text("火球");
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("可以远距离破坏材质为木板或羊毛的桥。");
    }
}