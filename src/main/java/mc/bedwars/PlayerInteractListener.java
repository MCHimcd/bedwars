package mc.bedwars;

import mc.bedwars.factory.Message;
import mc.bedwars.game.GameState;
import mc.bedwars.game.PlayerData;
import mc.bedwars.game.card.equips.IronAxe;
import mc.bedwars.game.card.equips.Pickaxe;
import mc.bedwars.game.card.equips.Scissors;
import mc.bedwars.game.map.node.Road;
import mc.bedwars.game.map.node.island.Island;
import mc.bedwars.game.map.node.island.resource.Bed;
import mc.bedwars.menu.MainMenu;
import mc.bedwars.menu.game.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.SkullMeta;

import static mc.bedwars.game.GameState.*;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
            return;

        var item = e.getItem();
        var p = e.getPlayer();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasCustomModelData()) return;

        var id = item.getItemMeta().getCustomModelData();
        if (id == 60000) {
            p.openInventory(new MainMenu(p).getInventory());
            return;
        }

        if (!started) return;

        var pd = players_data.get(p);

        switch (id) {
            case 60009 -> openChoosePlayerMenu(p, pd);
            case 60007 -> handleMove(p, pd);
            case 60003 -> handleBuildBridge(p, pd);
            case 60008 -> p.openInventory(new CardMenu(p, 0).getInventory());
            case 60002 -> handlePvP(p, pd);
            case 60006 -> handleBreak(p, pd);
            case 60005 -> openShopMenu(p);
            case 60010 -> handleDestroyBed(p, pd);
            case 60011 -> skipTurn(p, pd);
        }
    }

    private void openChoosePlayerMenu(Player p, PlayerData pd) {
        p.openInventory(new ChoosePlayerMenu(
                p, pd.location.players.stream().filter(player -> !player.equals(p)).toList(),
                (it, pl) -> {
                    SkullMeta meta = (SkullMeta) it.getItemMeta();
                    var target = (Player) meta.getOwningPlayer();
                    pd.setTarget(target);
                    p.closeInventory();
                    assert target != null;
                    p.showTitle(Title.title(Message.rMsg("<aqua>你选择的目标为:"), Message.rMsg("<rainbow>%s".formatted(target.getName()))));
                    p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
                }
        ).getInventory());
    }

    private void handleMove(Player p, PlayerData pd) {
        if (pd.target_location != null) {
            if (!map.tryMove(p, pd.location, pd.target_location)) {
                showTitleAndPlaySound(p, "<aqua>当前岛屿方向没路可走");
            }
        } else {
            showTitleAndPlaySound(p, "<aqua>请选择一个岛屿");
        }
    }

    private void showTitleAndPlaySound(Player p, String message) {
        p.showTitle(Title.title(Message.rMsg(message), Message.rMsg(".")));
        p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }

    private void handleBuildBridge(Player p, PlayerData pd) {
        if (pd.target_location != null) {
            Island i1 = (Island) pd.location;
            Island i2 = pd.target_location;
            if (Math.abs(i1.getX() - i2.getX()) == 1 || Math.abs(i1.getY() - i2.getY()) == 1) {
                p.openInventory(new BlockMenu(p, 0).getInventory());
            } else {
                showTitleAndPlaySound(p, "<aqua>请选择一个就近岛屿");
            }
        } else {
            showTitleAndPlaySound(p, "<aqua>请选择一个就近岛屿");
        }
    }

    private void handlePvP(Player p, PlayerData pd) {
        var t = pd.getTarget();
        if (t != null) {
            p.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1.5f);
            t.playSound(t, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1.5f);
            p.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(Message.rMsg("         %s和%s进行了一场<red>pvp".formatted(p.getName(), t.getName()))));
            GameState.pvp(p, t);
        } else {
            p.sendMessage(Component.text("清先选择目标"));
        }
    }

    /**
     * 破坏桥
     */
    private void handleBreak(Player p, PlayerData pd) {
        if (pd.target_location != null) {
            if (pd.location instanceof Road) return;
            Island i1 = (Island) pd.location;
            Island i2 = pd.target_location;
            if (Math.abs(i1.getX() - i2.getX()) == 1 || Math.abs(i1.getY() - i2.getY()) == 1) {
                map.roads.stream().filter(road -> road.hasNode(pd.location)).findFirst().ifPresent(
                        road -> {
                            if (hasRequiredTool(pd, road)) {
                                map.breakRoad(p, road);
                                p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
                                pd.addAction(-1);
                            } else {
                                showTitleAndPlaySound(p, "<aqua>你没有破坏这座桥的工具");
                            }
                        }
                );
            } else {
                showTitleAndPlaySound(p, "<aqua> 请选择一个就近岛屿 ");
            }
        } else {
            showTitleAndPlaySound(p, "<aqua> 请选择一个就近岛屿 ");
        }
    }

    private boolean hasRequiredTool(PlayerData pd, Road road) {
        return switch (road.getMaterial()) {
            case END_STONE -> pd.equipments.stream().anyMatch(em -> em instanceof Pickaxe);
            case WHITE_WOOL -> pd.equipments.stream().anyMatch(em -> em instanceof Scissors);
            case CRIMSON_PLANKS -> pd.equipments.stream().anyMatch(em -> em instanceof IronAxe);
            default -> false;
        };
    }

    private void openShopMenu(Player p) {
        p.openInventory(new ShopMenu(p).getInventory());
        p.playSound(p, Sound.ENTITY_VILLAGER_TRADE, 1f, 1.5f);
    }

    private void handleDestroyBed(Player p, PlayerData pd) {
        if (pd.location instanceof Bed b) {
            if (b.getOrder() != pd.getOrder()) {
                players_data.values().stream().filter(pld -> b.getOrder() == pld.getOrder()).findFirst().ifPresent(pld ->
                        p.openInventory(new ChooseDestroyBedBlockMenu(p).getInventory()));
            } else {
                p.openInventory(new ChoosePlaceBedBlockMenu(p).getInventory());
                p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
            }
        }
    }

    private void skipTurn(Player p, PlayerData pd) {
        pd.location.players.remove(p);
        map.moveTo(p, pd.location);
        p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
        p.playSound(p, Sound.UI_BUTTON_CLICK, 1f, .5f);
    }
}
