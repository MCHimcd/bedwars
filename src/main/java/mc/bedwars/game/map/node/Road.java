package mc.bedwars.game.map.node;

import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.Island;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import static mc.bedwars.game.map.GameMap.*;

public class Road extends Node {
    private final List<Node> nodes = new ArrayList<>();
    private Material material;

    public Road(Material m, Node... ns) {
        nodes.addAll(List.of(ns));
        setMaterial(m);
    }

    public boolean hasNode(Node node) {
        return nodes.contains(node);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material m) {
        material = m;
        replaceBlock(getLocation((Island) nodes.get(0)),getLocation((Island) nodes.get(1)),m);
        if(nodes.size()>2)
            replaceBlock(getLocation((Island) nodes.get(2)),getLocation((Island) nodes.get(3)),m);
    }

    public void setEnd(Node node) {
        nodes.set(1, node);
    }

    @Override
    public String getType() {
        return "road";
    }

}