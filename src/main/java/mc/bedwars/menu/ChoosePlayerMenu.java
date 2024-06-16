package mc.bedwars.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.function.BiConsumer;

public class ChoosePlayerMenu extends SlotMenu {
    public ChoosePlayerMenu(Player p, List<Player> players, BiConsumer<ItemStack, Player> function) {
        super(27, Component.text("选择玩家"), p);
        for (int i = 0; i < players.size(); i++) {
            int finalI = i;
            setSlot(i, new ItemStack(Material.PLAYER_HEAD) {{
                editMeta(m -> {
                    if (m instanceof SkullMeta sm) sm.setOwningPlayer(players.get(finalI));
                });
            }}, function);
        }
    }
}