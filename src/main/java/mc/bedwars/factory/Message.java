package mc.bedwars.factory;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.scoreboard.Scoreboard;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Message {
    public static Scoreboard h_board;
    public static final BossBar bar_h = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_10);
    public static final BossBar bar_time = BossBar.bossBar(Component.empty(), 1, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);

    public static final MiniMessage msg = MiniMessage.miniMessage();

    public static LinkedList<Component> convertMsg(List<String> sl) {
        return sl.stream().map(s -> {
            if (s.isEmpty()) return Component.empty();
            return msg.deserialize("<reset>" + s);
        }).collect(Collectors.toCollection(LinkedList::new));
    }

    public static Component rMsg(String s) {
        return msg.deserialize("<reset>" + s);
    }
}