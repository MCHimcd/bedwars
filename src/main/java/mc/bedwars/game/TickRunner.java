package mc.bedwars.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static mc.bedwars.game.GameState.*;

public class TickRunner extends BukkitRunnable {
    @Override
    public void run() {
        if (started){
            Bukkit.getServer().getOnlinePlayers().forEach(player ->{
                PlayerData playerData = players_data.get(player);
                int power = playerData.getPower();
                int maxPower = playerData.getMaxPower();
                if (power>maxPower)power=maxPower;
            });
        }

    }
}