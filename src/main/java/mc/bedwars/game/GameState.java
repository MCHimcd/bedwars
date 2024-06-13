package mc.bedwars.game;

import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.resource.Resource;
import mc.bedwars.game.player.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public final class GameState {
    public static boolean started = false;
    public static Map<Player, PlayerData> players_data = new Hashtable<>();
    public static GameMap map;
    public static int order = 1;

    public static void reset() {
        //重置
        players_data.clear();
        order = 1;
        map = new GameMap();
        started = false;
    }

    public static void start(List<Player> players) {
        //开始
        players.forEach(PlayerData::new);
        started = true;
    }

    public static void end() {
        //结束
        players_data.keySet().stream().findFirst().ifPresent(winner -> {
            winner.sendMessage(Component.text("§a你获得最终胜利！"));
        });
        reset();
    }

    public static void nextPlayer() {
        if (++order == 5) {
            nextTurn();
        }
        players_data.forEach((p, d) -> {
            if (d.getOrder() == order) {
                p.addScoreboardTag("action");
            } else p.removeScoreboardTag("action");
        });
    }

    public static void nextTurn() {
        order = 1;
        map.nodes.forEach(node -> {
            if (node instanceof Resource r) r.generate();
        });
    }

}