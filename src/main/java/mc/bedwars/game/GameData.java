package mc.bedwars.game;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GameData {
    public static Map<Player, PlayerData> players_data = new HashMap<>();

    public static void reset() {
        //重置
        players_data.clear();
    }

    public static void start(List<Player> players) {
        //开始
        players.forEach(PlayerData::new);
    }

    public static void end() {
        //结束
        reset();
    }
}