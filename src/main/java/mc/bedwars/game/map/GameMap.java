package mc.bedwars.game.map;

import mc.bedwars.factory.Message;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Blocks.Wool;
import mc.bedwars.game.card.props.EnderPearl;
import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.game.map.node.island.Grass;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.game.map.node.island.Platform;
import mc.bedwars.game.map.node.island.resource.*;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.bedwars.game.GameState.players_data;
import static mc.bedwars.game.GameState.map;

//游戏地图
public class GameMap {
    private static final List<Material> materials=new ArrayList<>(List.of(Material.WHITE_WOOL,Material.CRIMSON_PLANKS,Material.END_STONE,Material.OBSIDIAN,Material.BARRIER));
    public final Map<TextDisplay, Node> markers = new HashMap<>();
    public final List<Road> roads = new ArrayList<>();
    /*
    金。绿。金
    。钻草钻。
    蓝草绿草红
    。钻草钻。
    金。黄。金
    */
    public List<Island> islands = new ArrayList<>(List.of(
            new Gold(0, 0),
            new Gold(0, 4),
            new Gold(4, 0),
            new Gold(4, 4),
            new Bed(0, 2, 3),
            new Bed(2, 0, 2),
            new Bed(2, 4, 4),
            new Bed(4, 2, 1),
            new Diamond(1, 1),
            new Diamond(3, 3),
            new Diamond(3, 1),
            new Diamond(1, 3),
            new Grass(2, 1),
            new Grass(1, 2),
            new Grass(3, 2),
            new Grass(2, 3),
            new Platform(0,1),
            new Platform(0,3),
            new Platform(1,0),
            new Platform(3,0),
            new Platform(1,4),
            new Platform(3,4),
            new Platform(4,1),
            new Platform(4,3),
            new Emerald(2, 2)
    ));

    public GameMap(World world) {
        //生成markers
        for (var i : islands) {
            markers.put(world.spawn(getLocation(i), TextDisplay.class, marker -> {
                var t=marker.getTransformation();
                marker.setTransformation(new Transformation(t.getTranslation().add(0,5,0),t.getLeftRotation(),t.getScale().mul(4),t.getRightRotation()));
                marker.setBillboard(Display.Billboard.CENTER);
                marker.setDefaultBackground(true);
                marker.text(Component.text("%s岛".formatted(i.getType())));
            }), i);
        }
        //重置地图
        for (int i = -40; i<41; i++) {
            for (int j = -40; j < 41; j++) {
                var b=new Location(world,i,0,j).getBlock();
                if(b.getType()==Material.BARRIER) continue;
                else if(materials.contains(b.getType())) b.setType(Material.BARRIER);
            }
        }
    }

    public static void intoVoid(Player causer, Player p) {
        var pd = players_data.get(p);
        var ep = pd.items.stream().filter(item -> item instanceof EnderPearl).findFirst();
        if (ep.isPresent()) {
            ((EnderPearl)ep.get()).backHome(p);
        } else pd.die(List.of(causer));
    }

    public static Location getLocation(Island island) {
        return new Location(Bukkit.getWorld("world"), (island.getX() - 2) * 20+0.5, 1, (island.getY() - 2) * 20+0.5);
    }

    public void buildRoad(Island start, Island end, Material material) {
//         if (
//                (start instanceof Grass && end instanceof Grass)
//                        || (start instanceof Diamond && end instanceof Emerald)
//                        || (start instanceof Emerald && end instanceof Diamond)
//        ) {
//            roads.add(new Road(material, start, end, getIsland(2 * start.getX() - end.getX(), 2 * start.getY() - end.getY())));
//        } else
             var l1 = getLocation(start);
             var l2 = getLocation(end);
            replaceBlock(l1,l2,material);
            roads.add(new Road(material, start, end));

    }

    public static void replaceBlock(Location l1,Location l2,Material material) {
        l1.setY(0);
        l2.setY(0);
        var v1=l2.clone().subtract(l1).toVector().normalize();
        while (l1.getBlockX()!=l2.getBlockX()||l1.getBlockZ()!=l2.getBlockZ()){
            var block=l1.getBlock();
//            Bukkit.broadcast(Component.text( "%d %d".formatted(block.getX(),block.getZ())));
            if(materials.contains(block.getType())){
                block.setType(material);
            }
            l1.add(v1);
        }

    }

    public boolean move(Player p, Node start, Node end) {
        PlayerData pd = players_data.get(p);
        if (start.equals(end)) {
            start.players.remove(p);
            moveTo(p, end);//获得钱
            return true;
        } else if (pd.location instanceof Island island) {
            //在岛上
            var road = roads.stream().filter(r -> r.getMaterial() != Material.AIR && r.hasNode(start) && r.hasNode(end)).findFirst();
            if (road.isPresent()) {
                island.players.remove(p);
                moveTo(p, road.get());
                var l1 = GameMap.getLocation(island);
                var l2 = GameMap.getLocation((Island) end);
                var l3 = new Location(l1.getWorld(), (l1.x() + l2.x()) / 2, 1, (l1.z() + l2.z()) / 2);
                pd.getMarker().teleport(l3);
                return true;
            }
        } else if (start instanceof Road r) {
            //在桥上
            if (r.hasNode(end)){
                r.players.remove(p);
                moveTo(p, end);
                var l = GameMap.getLocation((Island) end);
                pd.getMarker().teleport(l);
                return true;
            }
        }
        return false;
    }

    private void moveTo(Player p, Node end) {
        PlayerData pd = players_data.get(p);
        pd.location = end;
        if (end instanceof Resource r) {
            r.giveMoney(p);
        }
        end.players.add(p);
        pd.addAction(-1);
    }

    public void breakRoad(Player p, Road r) {
        r.setMaterial(Material.AIR);
        r.players.forEach(player -> {
            PlayerData pd = players_data.get(player);
            var wool = pd.items.stream().filter(item -> item instanceof Wool).findFirst();
            if (wool.isPresent()) {
                r.setMaterial(Material.WHITE_WOOL);
                pd.items.remove(wool.get());
            } else intoVoid(p, player);
        });
        p.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(Component.text("<S>     %s破坏了一座桥;".formatted(p.getName()))));
    }

    public static Island getIsland(int x, int y) {
        var i = map.islands.stream().filter(island -> island.getX() == x && island.getY() == y).findFirst();
        return i.orElse(null);
    }
}