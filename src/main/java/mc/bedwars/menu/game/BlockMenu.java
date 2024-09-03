package mc.bedwars.menu.game;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.Blocks.isBlock;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.menu.SlotMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.game.GameState.map;
import static mc.bedwars.game.GameState.players_data;

public class BlockMenu extends SlotMenu {
    public BlockMenu(Player p, int startIndex) {
        super(27, Component.text("方块"), p);
        PlayerData pd = players_data.get(p);
        List<Card> cards = pd.items.stream().filter(card -> card instanceof isBlock).skip(startIndex).toList();

        for (int i = 0; i < Math.min(cards.size(), 25); i++) {
            Card card = cards.get(i);
            setSlot(i - startIndex, ItemCreator.create(Material.PAPER).name(card
                                    .Name())
                            .data(card.CustomModelData())
                            .lore(card.Lore())
                            .hideAttributes().getItem(),
                    (it, pl) -> {
                        var pd1 = players_data.get(p);
                        var success = true;

                        var m = ((isBlock) card).material();
                        var road = map.roads.stream().filter(r -> r.hasNode(pd1.location) && r.hasNode(pd1.target_location)).findFirst();
                        if (road.isPresent()) {
                            if (road.get().getMaterial() == m) {
                                pl.sendMessage(Component.text("该位置已有指定方块的桥"));
                                success = false;
                            } else {
                                road.get().setMaterial(m);
                            }
                        } else {
                            map.roads.add(new Road(m, pd1.location, pd1.target_location));
                        }

                        if (success) {
                            p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1.5f);
                            pd.items.remove(card);
                            pd1.addAction(-1);
                        }
                    }
            );
        }

        if (cards.size() > 25) {
            setSlot(26, ItemCreator.create(Material.PAPER).name(Component.text("下一页")).data(99999).getItem(), (it, pl) -> {
                pl.openInventory(new BlockMenu(pl, 26).getInventory());
                pl.playSound(pl, Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
            });
        }
    }
}