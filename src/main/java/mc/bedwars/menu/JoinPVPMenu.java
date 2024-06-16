package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JoinPVPMenu extends SlotMenu{
    public JoinPVPMenu(Player p, List<Player> p1, List<Player> p2) {
        super(9,Component.empty(), p);
        setSlot(2, ItemCreator.create(Material.PAPER).name(Component.text("加入%s".formatted(p1.get(0)))).getItem(),(it, pl)->p1.add(pl));
        setSlot(6,ItemCreator.create(Material.PAPER).name(Component.text("加入%s".formatted(p2.get(0)))).getItem(),(it, pl)->p2.add(pl));
        setSlot(4,ItemCreator.create(Material.PAPER).name(Component.text("旁观")).getItem(),(it, pl)->{});
    }
}