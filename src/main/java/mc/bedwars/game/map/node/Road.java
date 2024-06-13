package mc.bedwars.game.map.node;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Road extends Node {
    private final List<Node> nodes = new ArrayList<>();
    private final Material material;

    public Road(Node n1, Node n2, Material m) {
        nodes.add(n1);
        nodes.add(n2);
        material=m;
    }

    public boolean hasNode(Node node) {
        return nodes.contains(node);
    }

    public Material getMaterial() {
        return material;
    }
}