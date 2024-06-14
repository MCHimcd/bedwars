package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.equips.IsTool;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.function.BiConsumer;

import static mc.bedwars.game.GameState.players_data;

public class DestoryBedMenu extends SlotMenu{
    public DestoryBedMenu(Player p, List<Card> cards) {
        super(27, Component.text("选择使用的工具"), p);
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card instanceof IsTool) {
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