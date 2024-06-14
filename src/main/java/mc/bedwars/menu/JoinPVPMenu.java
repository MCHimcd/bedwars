package mc.bedwars.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JoinPVPMenu extends SlotMenu{
    public JoinPVPMenu(Player p, List<Player> p1, List<Player> p2) {
        super(9,Component.empty(), p);
        setSlot(2,new ItemStack(Material.PAPER),(_, pl)->p1.add(pl));
        setSlot(6,new ItemStack(Material.PAPER),(_, pl)->p2.add(pl));
        setSlot(4,new ItemStack(Material.PAPER),(_, _)->{});
    }
}