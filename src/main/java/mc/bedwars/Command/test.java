package mc.bedwars.Command;

import mc.bedwars.BedWars;
import mc.bedwars.factory.particle;
import mc.bedwars.game.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        List<Location> graph = graph1(player);
        Location location = player.getLocation().add(3, 1, 0);
        return true;
    }
    //旋转圆
//    degree += 8;
//    double radians = Math.toRadians(degree);
//    Location a = player.getLocation().clone().add(r * Math.cos(radians), 1D, r * Math.sin(radians));
//    player.getWorld().spawnParticle(Particle.END_ROD, a, 0,0,0,0,0,null,true);

    public List<Location> graph1(Player player){
        List<Location> locations = new ArrayList<>();
        for (double a=-5;a<=5;a+=0.1){
            double y=Math.log(a*a)+a*a*0.003;
            Location loc = particle.roloc(player,a,y+1,0,0);
            Location loc1 = particle.roloc(player,0,y+1,-a,0);
            locations.add(loc);
            locations.add(loc1);
        }
        return locations;
    }
    public List<Location> graph(Player player){
        List<Location> locations = new ArrayList<>();
        for (double a=0;a<=6.29;a+=0.2){
            for (double b=0;b<=3.15;b+=0.1){
                double x =3 * Math.sin(b) * Math.cos(a);
                double z =3 * Math.sin(b) * Math.sin(a);
                double y =3 * Math.cos(b);
                Location loc = particle.roloc(player,x,y+1,z,0);
                locations.add(loc);
            }
        }
        return locations;
    }
}
