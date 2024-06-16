package mc.bedwars.game.map.node;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    public final List<Player> players = new ArrayList<>();

    public abstract String getType();

    @Override
    public String toString() {
        return getType();
    }
}