package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Blocks.isBlock;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.node.Road;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.game.GameState.map;
import static mc.bedwars.game.GameState.players_data;

public class BlockMenu extends SlotMenu {
    public BlockMenu(Player p) {
        super(27, Component.text("方块"), p);
        PlayerData pd = players_data.get(p);
        List<Card> cards = pd.items.stream().filter(card -> card instanceof isBlock).toList();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card instanceof isBlock) {
                setSlot(i, ItemCreator.create(Material.PAPER).name(card
                                        .Name())
                                .data(card.CustomModelData())
                                .lore(card.Introduction())
                                .hideAttributes().getItem(),
                        (it, pl) -> {
                            var pd1 = players_data.get(p);
                            pd1.items.remove(card);
                            map.roads.add(new Road(((isBlock) card).material(), pd1.location, pd1.target_location));
                            p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1.5f);
                            pd1.addAction(-1);
                        }
                );
            }
        }
    }
}