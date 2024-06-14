package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Blocks.isBlock;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.function.BiConsumer;

import static mc.bedwars.game.GameState.players_data;

public class PlaceBedMenu extends SlotMenu{
    public PlaceBedMenu(Player p) {
        super(27, Component.text("选择摆放或拆除的方块"), p);
        var pd = players_data.get(p);
        List<Card> cards = pd.items.stream().filter(card -> card instanceof isBlock).toList();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card instanceof isBlock) {
                setSlot(i, ItemCreator.create(Material.PAPER).name(card
                                        .Name())
                                .amount(card.itemMaxCount())
                                .data(card.CustomModelData())
                                .lore(card.Introduction())
                                .hideAttributes().getItem(),
                        (it, pl) -> {
                            card.effect(pl);
                            players_data.get(p).items.remove(card);
                        });
            }
        }
    }
}