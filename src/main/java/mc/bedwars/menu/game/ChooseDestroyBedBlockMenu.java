package mc.bedwars.menu.game;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.factory.Message;
import mc.bedwars.game.map.node.island.resource.Bed;
import mc.bedwars.menu.SlotMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

import static mc.bedwars.game.GameState.players_data;

public class ChooseDestroyBedBlockMenu extends SlotMenu {
    public ChooseDestroyBedBlockMenu(Player destroyer) {
        super(9, Component.text("选择破坏的方向"), destroyer);
        var pd_destroyer = players_data.get(destroyer);
        setSlot(0, ItemCreator.create(Material.PAPER).amount(1).name(Message.rMsg("<red><左边>")).getItem(),
                (it, pl) -> {
                    destroyer.closeInventory();
                    if (pd_destroyer.location instanceof Bed bed) {
                        AtomicInteger isnull = new AtomicInteger();
                        if (bed.getOrder() != pd_destroyer.getOrder()) {
                            players_data.values().stream().filter(pld -> bed.getOrder() == pld.getOrder()).findFirst().ifPresent(pld -> {
                                for (int j = 1; j < 4; j++) {
                                    if (pld.protectBedBlockMaterial(j) != Material.AIR) {
                                        pd_destroyer.setDestroyBedBlock(j);
                                        destroyer.openInventory(new DestroyBedMenu(destroyer, pd_destroyer.equipments, j).getInventory());
                                        destroyer.sendMessage(Message.rMsg("         当前破坏的层数为%s".formatted(j)));
                                        destroyer.playSound(destroyer, Sound.UI_BUTTON_CLICK, 1f, .5f);
                                        return;
                                    } else {
                                        isnull.getAndIncrement();
                                    }
                                    if (isnull.get() == 3) {
                                        pld.destroyBed();
                                    }
                                }
                            });
                        }
                    }
                });

        for (int i = 1; i <= 3; i++) {
            if (pd_destroyer.protectBedBlockMaterial(i) != Material.AIR) {
                setSlot(i, ItemCreator.create(Material.BLACK_STAINED_GLASS).getItem(), (it, pl) -> {
                });
            } else {
                setSlot(i, ItemCreator.create(Material.BARRIER).getItem(), (it, pl) -> {
                });
            }
        }

        for (int i = 5; i <= 7; i++) {
            int c = i - 1;
            if (pd_destroyer.protectBedBlockMaterial(c) != Material.AIR) {
                setSlot(i, ItemCreator.create(Material.BLACK_STAINED_GLASS).getItem(), (it, pl) -> {
                });
            } else {
                setSlot(i, ItemCreator.create(Material.BARRIER).getItem(), (it, pl) -> {
                });
            }
        }
        setSlot(8, ItemCreator.create(Material.PAPER).amount(1).name(Message.rMsg("<red><右边>")).getItem(),
                (it, pl) -> {
                    if (pd_destroyer.location instanceof Bed b) {
                        AtomicInteger isnull = new AtomicInteger();
                        if (b.getOrder() != pd_destroyer.getOrder()) {
                            players_data.values().stream().filter(pld -> b.getOrder() == pld.getOrder()).findFirst().ifPresent(pld -> {
                                for (int j = 4; j < 7; j++) {
                                    if (pld.protectBedBlockMaterial(j) != Material.AIR) {
                                        pd_destroyer.setDestroyBedBlock(j);
                                        destroyer.openInventory(new DestroyBedMenu(destroyer, pd_destroyer.equipments, j).getInventory());
                                        destroyer.sendMessage(Message.rMsg("          当前破坏的层数为%s".formatted(j)));
                                        destroyer.playSound(destroyer, Sound.UI_BUTTON_CLICK, 1f, .5f);
                                        return;
                                    } else {
                                        isnull.getAndIncrement();
                                    }
                                    if (isnull.get() == 3) {
                                        pld.destroyBed();
                                    }
                                }
                            });
                        }
                    }
                });
    }
}