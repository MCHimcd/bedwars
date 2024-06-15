package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.factory.Message;
import mc.bedwars.game.map.node.island.resource.Bed;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.players_data;

public class ChooseDestoryBedBlockMenu extends SlotMenu {
    public ChooseDestoryBedBlockMenu(Player p) {
        super(9, Component.text("选择破坏的方向"), p);
        var pd = players_data.get(p);
        setSlot(0, ItemCreator.create(Material.PAPER).amount(1).name(Message.rMsg("<red><左边>")).getItem(),
                (it, pl) -> {
                    p.closeInventory();
                    if (pd.location instanceof Bed b) {
                        if (b.getOrder() != pd.getOrder()) {
                            players_data.values().stream().filter(pld -> b.getOrder() == pld.getOrder()).findFirst().ifPresent(pld -> {
                                for (int j = 0; j < 2; j++) {
                                    if (pld.protectBedBlockMaterial(j) != Material.AIR) {
                                        pd.setDestroyBedBlock(j);
                                        p.openInventory(new DestoryBedMenu(p, pd.equipments,j).getInventory());
                                        p.sendMessage(Message.rMsg("     当前破坏的层数为%s".formatted(j)));
                                        p.playSound(p, Sound.UI_BUTTON_CLICK,1f,.5f);
                                        break;
                                    }else {
                                        pld.destroyBed();
                                    }
                                }
                            });
                        }
                    }
                });
        setSlot(8, ItemCreator.create(Material.PAPER).amount(1).name(Message.rMsg("<red><右边>")).getItem(),
                (it, pl) -> {
                    if (pd.location instanceof Bed b) {
                        if (b.getOrder() != pd.getOrder()) {
                            players_data.values().stream().filter(pld -> b.getOrder() == pld.getOrder()).findFirst().ifPresent(pld -> {
                                for (int j = 3; j < 5; j++) {
                                    if (pld.protectBedBlockMaterial(j) != Material.AIR) {
                                        pd.setDestroyBedBlock(j);
                                        p.openInventory(new DestoryBedMenu(p, pd.equipments,j).getInventory());
                                        p.sendMessage(Message.rMsg("     当前破坏的层数为%s".formatted(j)));
                                        p.playSound(p, Sound.UI_BUTTON_CLICK,1f,.5f);
                                        break;
                                    }else {
                                        pld.destroyBed();
                                    }
                                }
                            });
                        }
                    }
                });
    }
}
