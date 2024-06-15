package mc.bedwars.Command;

import mc.bedwars.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class start implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        var ps=new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(ps);
        GameState.start(new ArrayList<>(ps));
        return true;
    }
}