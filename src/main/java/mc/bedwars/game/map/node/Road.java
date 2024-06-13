package mc.bedwars.game.map.node;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Road extends Node {
    private static int id_g = 0;
    private final List<Node> nodes = new ArrayList<>();
    private Material material;
    private int id = 0;

    public Road(Node n1, Node n2, Material m) {
        nodes.add(n1);
        nodes.add(n2);
        material = m;
        id = id_g++;
    }

    public boolean hasNode(Node node) {
        return nodes.contains(node);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material m) {
        material = m;
    }

    public void setEnd(Node node) {
        nodes.set(1, node);
    }

    @Override
    public String getType() {
        return "road";
    }

    public int getId() {
        return id;
    }
}