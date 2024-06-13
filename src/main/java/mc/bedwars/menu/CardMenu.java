package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.props.needTarget;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.game.GameState.players_data;

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
                        (it, pl) -> {
                            if (card instanceof needTarget) {
                                if (players_data.get(p).getTarget() != null) {
                                    card.effect(pl);
                                    players_data.get(p).items.remove(card);
                                } else {
                                    p.sendMessage(Component.text("<S>      你需要一个目标才可使用%s.".formatted(card.Name())));
                                }
                            } else {
                                card.effect(pl);
                                players_data.get(p).items.remove(card);
                            }
                        });
            }
        }
    }
}