package mc.bedwars.game.map.node;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Road extends Node {
    private final List<Node> nodes = new ArrayList<>();
    private Material material;

    public Road(Material m, Node... ns) {
        nodes.addAll(List.of(ns));
        material = m;
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

}