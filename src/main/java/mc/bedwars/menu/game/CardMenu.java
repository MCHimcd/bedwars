package mc.bedwars.menu.game;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.props.*;
import mc.bedwars.game.map.node.Node;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.menu.SlotMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.game.GameState.players_data;

public class CardMenu extends SlotMenu {
    public CardMenu(Player p, int startIndex) {
        super(27, Component.text("卡"), p);
        var pd = players_data.get(p);
        List<Card> cards = pd.items.stream()
                .filter(card -> card instanceof Prop)
                .skip(startIndex)
                .toList();

        int slotsToFill = Math.min(cards.size(), 25);
        for (int i = 0; i < slotsToFill; i++) {
            Card card = cards.get(i);
            setSlot(i, ItemCreator.create(Material.PAPER)
                    .name(card.Name())
                    .data(card.CustomModelData())
                    .lore(card.Lore())
                    .hideAttributes()
                    .getItem(), (it, pl) -> handleCardAction(pl, card));
        }

        if (cards.size() > 25) {
            setSlot(26, ItemCreator.create(Material.PAPER)
                    .name(Component.text("下一页"))
                    .data(99999)
                    .getItem(), (it, pl) -> {
                pl.openInventory(new CardMenu(pl, startIndex + 25).getInventory());
                pl.playSound(pl, Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
            });
        }
    }

    private void handleCardAction(Player p, Card card) {
        var pd = players_data.get(p);
        Node currentNode = pd.location;

        Island targetIsland = pd.target_location;

        boolean hasTarget = pd.getTarget() != null;
        boolean hasIslandTarget = targetIsland != null;

        if (card instanceof NeedTarget && !hasTarget) {
            sendMessage(p, card, "你需要一个目标才可使用%s.");
            return;
        }

        if ((card instanceof BridgeEgg || card instanceof Fireball || card instanceof Tnt || card instanceof EnderPearl) && !hasIslandTarget) {
            sendMessage(p, card, "你需要一个岛屿目标才可使用%s.");
            return;
        }

        if (card.effect(p)) {
            p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
            pd.items.remove(card);
            pd.resetInventoryItems();
        }
    }

    private void sendMessage(Player p, Card card, String message) {
        p.sendMessage(Component.text(String.format(message, PlainTextComponentSerializer.plainText().serialize(card.Name()))));
    }
}