package mc.bedwars.game.card.props;

import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.game.map.node.island.Island;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.map;
import static mc.bedwars.game.GameState.players_data;
import static mc.bedwars.game.map.GameMap.getIsland;

public class BridgeEgg extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        var pd = players_data.get(player);
        Island i1 = (Island) pd.location;
        Island i2 = pd.target_location;
        int dx = Math.abs(i1.getX() - i2.getX());
        int dy = Math.abs(i1.getY() - i2.getY());

        // 检查无效移动
        if (dx > 2 || dy > 2 || (dx == 2 && dy == 1) || (dx == 1 && dy == 2)) return false;

        if (dx == 1 || dy == 1) {
            // 直接相邻岛屿
            createRoadAndNotify(player, i1, i2, pd);
        } else if (dx == 2 || dy == 2) {
            // 间接相邻岛屿
            Island middle = getMiddleIsland(i1, i2, dx, dy);
            createRoadAndNotify(player, i1, middle, pd);
            createRoadAndNotify(player, middle, i2, pd);
        } else {
            return false;
        }

        pd.addAction(-1);
        return true;
    }

    private void createRoadAndNotify(Player player, Island i1, Island i2, PlayerData pd) {
        map.roads.add(new Road(Material.WHITE_WOOL, i1, i2));
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, pd.getMarker().getLocation(), 100, 0.5, 1, 0.5, 0.3, null, true);
        player.getWorld().sendMessage(Component.text("           §l%s使用了 §1搭桥蛋".formatted(player.getName())));
        player.playSound(player, Sound.ENTITY_EGG_THROW, 1f, 1f);
    }

    private Island getMiddleIsland(Island i1, Island i2, int dx, int dy) {
        if (dx == 2 && dy != 2) {
            return getIsland(i1.getX() + (i2.getX() - i1.getX()) / 2, i2.getY());
        } else if (dy == 2 && dx != 2) {
            return getIsland(i2.getX(), i1.getY() + (i2.getY() - i1.getY()) / 2);
        } else {
            return getIsland(i1.getX() + (i2.getX() - i1.getX()) / 2, i1.getY() + (i2.getY() - i1.getY()) / 2);
        }
    }


    @Override
    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 12;
    }

    @Override
    public int itemMaxCount() {
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 20002;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return Component.text("搭桥蛋");
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("在两座岛屿之间建立材质为羊毛的桥");
    }
}