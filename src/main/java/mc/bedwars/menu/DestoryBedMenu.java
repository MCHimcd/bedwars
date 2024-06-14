package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.equips.IronAxe;
import mc.bedwars.game.card.equips.Tool;
import mc.bedwars.game.card.equips.Pickaxe;
import mc.bedwars.game.card.equips.Scissors;
import mc.bedwars.game.map.node.island.resource.Bed;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static mc.bedwars.game.GameState.players_data;

public class DestoryBedMenu extends SlotMenu {
    public DestoryBedMenu(Player p, List<Card> cards) {
        super(27, Component.text("选择使用的工具"), p);
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card instanceof Tool) {
                setSlot(i, ItemCreator.create(Material.PAPER).name(card
                                        .Name())
                                .amount(card.itemMaxCount())
                                .data(card.CustomModelData())
                                .lore(card.Introduction())
                                .hideAttributes().getItem(),
                        (it, pl) -> {
                    var pd=players_data.get(pl);
                    Bed b= (Bed) pd.location;
                            players_data.values().stream().filter(pld -> b.getOrder() == pld.getOrder()).findFirst().ifPresent(pld -> {
                                if(switch (pld.protectBedBlockMaterial(pd.getDestroyBedBlock())){
                                    case Material.END_STONE -> card instanceof Pickaxe;
                                    case WHITE_WOOL -> card instanceof Scissors;
                                    case Material.CRIMSON_PLANKS -> card instanceof IronAxe;
                                    default -> false;
                                }) {
                                    pld.setProtectBed(pd.getDestroyBedBlock());
                                    pld.placeBedBlock(Material.AIR);
                                    p.sendMessage(Component.text("<S>     正确的的选择，该玩家%s层保护为%s".formatted(pd.getDestroyBedBlock(),pld.protectBedBlockMaterial(pd.getDestroyBedBlock()))));
                                } else {
                                    pd.addAction(-1);
                                    p.sendMessage(Component.text("<S>     错误的选择，该玩家%s层保护为%s".formatted(pd.getDestroyBedBlock(),pld.protectBedBlockMaterial(pd.getDestroyBedBlock()))));
                                }
                            });
                        });
            }
        }
    }
}