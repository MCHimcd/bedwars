package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.Island;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.map;
import static mc.bedwars.game.GameState.players_data;

public class Tnt extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        var pd = players_data.get(player);
        if (pd.target_location_1 == null) {
            pd.target_location_1 = pd.target_location;
            player.sendMessage(Message.rMsg("<aqua>选择下一个岛"));
        } else {
            Island i1 = pd.target_location_1;
            pd.target_location_1 = null;
            var road = map.roads.stream().filter(r -> r.hasNode(pd.target_location) && r.hasNode(i1)).findFirst();
            if (road.isPresent()) {
                if (switch (road.get().getMaterial()) {
                    case WHITE_WOOL, CRIMSON_PLANKS, END_STONE -> true;
                    default -> false;
                }) {
                    Bukkit.broadcast(Component.text("           §l%s使用了 §1TNT".formatted(player.getName())));
                    var l1 = GameMap.getLocation(i1);
                    var l2 = GameMap.getLocation(pd.target_location);
                    var l3 = new Location(player.getWorld(), (l1.getX() + l2.getX()) / 2, 0, (l1.getZ() + l2.getZ()) / 2);
                    player.getWorld().spawnParticle(Particle.EXPLOSION, l3, 1);
                    player.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
                    pd.addAction(-1);
                    map.breakRoad(player, road.get());
                    return true;
                }
            } else {
                player.sendMessage(Message.rMsg("<aqua>请重新选择"));
            }
        }
        return false;
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
        return 999;
    }

    @Override
    public int CustomModelData() {
        return 20001;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<red>TNT");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>破坏:可以破坏当前岛屿或与当前",
                "<white>岛屿相邻岛屿四周的桥或材质为",
                "<white>羊毛 木板 末地石 的方块",
                "",
                "<aqua>每次购买数量:1",
                "<green>经济:16"
        ));
    }

    @Override
    public boolean CanUse() {
        return true;
    }
}