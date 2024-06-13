package mc.bedwars.factory;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class particle {
    //朝向
    public static @NotNull Vector getv(Location a, Location b) {
        return a.clone().subtract(b).toVector();
    }


    //转向
    public static Location roloc(Player player, double x, double y, double z, double rd1){
        float Yaw = player.getEyeLocation().getYaw();
        double rd = Math.toRadians(Yaw)+rd1;
        Location o =player.getLocation();
        o.setPitch(0);
        double x1 = player.getLocation().getX()+x;
        double y1 = player.getLocation().getY()+y;
        double z1 = player.getLocation().getZ()+z;
        double dx = x1 -= o.getX();
        double dz = z1 -= o.getZ();
        double newX = dx * Math.cos(rd) - dz * Math.sin(rd) + o.getX();
        double newZ = dz * Math.cos(rd) + dx * Math.sin(rd) + o.getZ();
        return new Location(player.getWorld(),newX,y1,newZ);
    }
}
