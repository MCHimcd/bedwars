package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.props.*;
import mc.bedwars.game.map.node.island.Island;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.game.GameState.players_data;

public class CardMenu extends SlotMenu {
    public CardMenu(Player p, List<Card> cards) {
        super(27, Component.text("卡"), p);
        var pd=players_data.get(p);

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card instanceof Prop) {
                setSlot(i, ItemCreator.create(Material.PAPER).name(card
                                        .Name())
                                .amount(card.itemMaxCount())
                                .data(card.CustomModelData())
                                .lore(card.Introduction())
                                .hideAttributes().getItem(),
                        (it, pl) -> {
                            Island i1 = (Island) pd.location;
                            Island i2 = pd.target_location;
                            switch (card) {
                                case NeedTarget needTarget -> {
                                    if (players_data.get(p).getTarget() != null){
                                            if(card.effect(pl)) {
                                                p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                                pd.items.remove(card);
                                                pd.resetInventoryItems();
                                            }
                                    } else {
                                        p.sendMessage(Component.text("         你需要一个目标才可使用%s.".formatted(card.Name())));
                                    }
                                }
                                case BridgeEgg bridgeEgg -> {
                                    if (pd.target_location != null) {
                                        if(card.effect(pl)){
                                            p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                            pd.items.remove(card);
                                            pd.resetInventoryItems();
                                        }
                                    } else {
                                        p.sendMessage(Component.text("         你需要一个岛屿目标才可使用%s.".formatted(card.Name())));
                                    }
                                }
                                case Fireball fireball -> {
                                    if (pd.target_location != null) {
                                        if(card.effect(pl)) {
                                            pd.items.remove(card);
                                            pd.resetInventoryItems();
                                        }
                                        p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                    } else {
                                        p.sendMessage(Component.text("        你需要一个岛屿目标才可使用%s.".formatted(card.Name())));
                                    }
                                }
                                case EnderPearl enderPearl -> {
                                    if (pd.target_location != null) {
                                        card.effect(pl);
                                        p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                        pd.items.remove(card);
                                        pd.resetInventoryItems();
                                    } else {
                                        p.sendMessage(Component.text("         你需要一个岛屿目标才可使用%s.".formatted(card.Name())));
                                    }
                                }
                                case Tnt tnt -> {
                                    if (Math.abs(i1.getX() - i2.getX()) == 1 || Math.abs(i1.getY() - i2.getY()) == 1) {
                                        if (pd.target_location != null) {
                                            card.effect(pl);
                                            p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                            pd.items.remove(card);
                                            pd.resetInventoryItems();
                                        } else {
                                            p.sendMessage(Component.text("         你需要一个岛屿目标才可使用%s.".formatted(card.Name())));
                                        }
                                    }else p.sendMessage(Component.text("   t      你需要选择相邻的岛屿目标才可使用%s.".formatted(card.Name())));
                                }
                                default -> {
                                    p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.5f);
                                    card.effect(pl);
                                    pd.items.remove(card);
                                    pd.resetInventoryItems();
                                }
                            }
                        });
            }
        }
    }
}