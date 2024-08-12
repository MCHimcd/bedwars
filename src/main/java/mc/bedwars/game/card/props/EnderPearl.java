package mc.bedwars.game.card.props;

import mc.bedwars.factory.Message;
import mc.bedwars.game.GameState;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.resource.Bed;
import net.kyori.adventure.text.Component;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.factory.Message.rMsg;
import static mc.bedwars.game.GameState.map;

public class EnderPearl extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        var pd = GameState.players_data.get(player);
        pd.addAction(1);
        pd.location.players.remove(player);
        pd.location = pd.target_location;
        map.moveTo(player, pd.target_location);
        player.getWorld().spawnParticle(Particle.WITCH, pd.getMarker().getLocation(), 100, 0.5, 1, 0.5, 0.3, null, true);
        var dxz = pd.getOrder() <= 2 ? (pd.getOrder() - 3) * 0.5 : (pd.getOrder() - 2) * 0.5;
        pd.getMarker().teleport(GameMap.getLocation(pd.target_location).setDirection(pd.getMarkerDirection()).add(dxz, 0, dxz));

        player.getInventory().clear();
        pd.resetInventoryItems();
        var is = pd.getActions();
        for (int i = 0; i < is.size(); i++) {
            player.getInventory().setItem(i, is.get(i));
        }

        player.getWorld().spawnParticle(Particle.WITCH, pd.getMarker().getLocation(), 100, 0.5, 1, 0.5, 0.3, null, true);
        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1f, 1f);
        player.getWorld().sendMessage(Component.text("                 §l%s使用了 §7末影之眼".formatted(player.getName())));
        return true;
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
        return 20003;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return rMsg("<red>末影珍珠");
    }

    @Override
    public List<Component> Lore() {
        return Message.convertMsg(List.of(
                "",
                "<white>瞬移:可以不消耗行动点移动到版",
                "<white>图任意岛屿",
                "<white>自救:掉入虚空可以回到原来所在岛屿或桥并立即获得1行动点",
                "",
                "<aqua>每次购买数量:1",
                "<green>经济:16"
        ));
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    public void backHome(Player player) {
        var pd = GameState.players_data.get(player);
        map.islands.stream().filter(island -> island instanceof Bed b && b.getOrder() == pd.getOrder()).findFirst().ifPresent(island -> {
            pd.location = island;
            var dxz = pd.getOrder() <= 2 ? (pd.getOrder() - 3) * 0.5 : (pd.getOrder() - 2) * 0.5;
            pd.getMarker().teleport(GameMap.getLocation(island).add(dxz, 0, dxz));
            pd.items.remove(this);
            pd.resetInventoryItems();
        });
    }
}