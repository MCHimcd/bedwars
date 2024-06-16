package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.players_data;

public class ChoosePlaceBedBlockMenu extends SlotMenu {
    public ChoosePlaceBedBlockMenu(Player p) {
        super(9, Component.text("选择放置的层数"), p);
        var pd = players_data.get(p);
        for (int i = 1; i < 7; i++) {
            Material material = pd.protectBedBlockMaterial(i);
            if (material == Material.AIR) {
                material = Material.GLASS_PANE;
            }
            setSlot(i, ItemCreator.create(material).amount(i).getItem(),
                    (it, pl) -> {
                        pd.setProtectBed(it.getAmount());
                        p.closeInventory();
                        p.openInventory(new PlaceBedMenu(p).getInventory());
                    });
        }
    }
}