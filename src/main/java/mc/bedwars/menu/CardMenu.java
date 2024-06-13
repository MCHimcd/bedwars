package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.props.needTarget;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class CardMenu extends SlotMenu {
    public CardMenu(Player p, List<Card> cards) {
        super(27, Component.text("卡"), p);

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card.CanUse()) {
                setSlot(i, ItemCreator.create(Material.PAPER).name(card
                                        .Name())
                                .amount(card.itemMaxCount())
                                .data(card.CustomModelData())
                                .lore(card.Introduction())
                                .hideAttributes().getItem(),
                        (it, pl) -> card.effect(pl));
            }
        }
    }
}