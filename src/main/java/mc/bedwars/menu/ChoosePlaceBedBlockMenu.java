package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Blocks.isBlock;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.props.isProps;
import mc.bedwars.game.card.props.needTarget;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.game.GameState.players_data;

public class ChoosePlaceBedBlockMenu extends SlotMenu{
    public ChoosePlaceBedBlockMenu(Player p) {
        super(9, Component.text("选择放置的层数"), p);
        var pd = players_data.get(p);
        for (int i = 0; i < 2; i++) {
                setSlot(i, ItemCreator.create(pd.protectBedBlockMaterial(i)).amount(i+1).getItem(),
                        (it, pl) -> {
                    pd.setProtectBed(it.getAmount());
                    p.closeInventory();
                    p.openInventory(new PlaceBedMenu(p).getInventory());
                        });
        }
    }
}
