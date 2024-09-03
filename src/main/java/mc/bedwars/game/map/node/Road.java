package mc.bedwars.game.map.node;

import mc.bedwars.game.map.node.island.Island;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

import static mc.bedwars.game.map.GameMap.getLocation;
import static mc.bedwars.game.map.GameMap.replaceBlock;

public class Road extends Node {
    private final List<Node> nodes = new ArrayList<>();
    private Material material;

    public Road(Material m, Node... ns) {
        nodes.addAll(List.of(ns));
        setMaterial(m);
    }

    /**
     * @return 该路两端是否含有指定的node
     */
    public boolean hasNode(Node node) {
        return nodes.contains(node);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material m) {
        material = m;
        replaceBlock(getLocation((Island) nodes.get(0)), getLocation((Island) nodes.get(1)), m);
    }

    @Override
    public String getType() {
        return "road";
    }

}