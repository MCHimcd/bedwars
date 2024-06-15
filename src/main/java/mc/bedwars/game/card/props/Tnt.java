package mc.bedwars.game.card.props;

import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.node.island.Island;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.map;
import static mc.bedwars.game.GameState.players_data;

public class Tnt extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        var pd = players_data.get(player);
        Island i1 = (Island) pd.location;
        Island i2 = (Island) pd.target_location;
        if (Math.abs(i1.getX() - i2.getX()) == 1 || Math.abs(i1.getY() - i2.getY()) == 1) {
            map.roads.stream().filter(road -> road.hasNode(pd.location)).findFirst().ifPresent(
                    road -> {
                        if (switch (road.getMaterial()) {
                            case END_STONE, CRIMSON_PLANKS, WHITE_WOOL -> true;
                            default -> false;
                        }) map.breakRoad(player, road);
                    }
            );
            player.getWorld().sendMessage(Component.text("<S>      §l%s使用了 §cTNT".formatted(player.getName())));
        }
        return true;
    }

    ;
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