package mc.bedwars.game;

import mc.bedwars.game.map.GameMap;
import org.bukkit.entity.Player;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public final class GameState {
    public static Map<Player, PlayerData> players_data = new Hashtable<>();
    public static GameMap map;
    public static int order = 1;

    public static void reset() {
        //重置
        players_data.clear();
        order = 1;
        map = new GameMap();
    }

    public static void start(List<Player> players) {
        //开始
        players.forEach(PlayerData::new);
    }

    public static void end() {
        //结束
        players_data.keySet().stream().findFirst().ifPresent(winner -> {
            winner.sendMessage("§a你赢了！");
        });
        reset();
    }

    public static void next() {
        order++;
        players_data.forEach((p, d) -> {
            if (d.getOrder() == order) {
                p.addScoreboardTag("action");
            } else p.removeScoreboardTag("action");
        });
    }
}