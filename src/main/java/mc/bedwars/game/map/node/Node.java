package mc.bedwars.game.map.node;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Node implements Cloneable {
    public final List<Player> players = new ArrayList<>();

    @Override
    public Node clone() {
        try {
            return (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getType();
    }

    public abstract String getType();
}