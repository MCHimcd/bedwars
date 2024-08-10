package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Blocks.*;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.card.boost.HealingSpring;
import mc.bedwars.game.card.boost.Protection;
import mc.bedwars.game.card.boost.Sharp;
import mc.bedwars.game.card.equips.*;
import mc.bedwars.game.card.props.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static mc.bedwars.game.GameState.players_data;

public class ShopMenu extends SlotMenu {

    public ShopMenu(Player p) {
        super(27, Component.text("商店"), p);
        Map<Card, ItemStack> items = new LinkedHashMap<>() {{
            List.of(
                    new Wool(),
                    new Plank(),
                    new EndStone(),
                    new ExplosionProofGlass(),
                    new Obsidian(),
                    new HealingSpring(),
                    new Protection(),
                    new Sharp(),
                    new DiamondEquips(),
                    new DiamondSword(),
                    new IronAxe(),
                    new IronEquips(),
                    new IronSword(),
                    new Pickaxe(),
                    new Scissors(),
                    new BridgeEgg(),
                    new EnderPearl(),
                    new Fireball(),
                    new GoldenApple(),
                    new KbtStick(),
                    new PotionOfInvisibility(),
                    new PotionOfLeaping(),
                    new PotionOfSwiftness(),
                    new Tnt()
            ).forEach(card -> {
                put(card, ItemCreator.create(Material.PAPER).data(card.CustomModelData()).name(card.Name()).lore(card.Lore()).getItem());
            });
        }};
        AtomicInteger i = new AtomicInteger();
        items.forEach((card, itemStack) -> {
            setSlot(i.get(), itemStack, (it, pl) -> {
                var pd = players_data.get(pl);
                if (card instanceof Equip) {
                    if (pd.equipments.stream().filter(c -> c.Name().equals(card.Name())).count() >= card.itemMaxCount()) {
                        p.sendMessage(Component.text("             §a持有%s超过上限。".formatted(card.Name().toString())));
                        p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    } else {
                        if (pd.removeMoney(card.costMoney())) {
                            pd.equipments.add(card);
                            p.playSound(p, Sound.ENTITY_ITEM_PICKUP, 1f, 2f);
                            p.sendMessage(Component.text("           §6购买成功。"));
                            pd.resetInventoryItems();
                        } else {
                            p.sendMessage(Component.text("           §6金钱不足。"));
                            p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                    }
                } else {
                    if (pd.items.stream().filter(c -> c.Name().equals(card.Name())).count() >= card.itemMaxCount()) {
                        p.sendMessage(Component.text("             §a持有%s超过上限。".formatted(card.Name().toString())));
                        p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    } else {
                        if (pd.removeMoney(card.costMoney())) {
                            p.playSound(p, Sound.ENTITY_ITEM_PICKUP, 1f, 2f);
                            p.sendMessage(Component.text("           §6购买成功。"));
                            pd.items.add(card);
                            pd.resetInventoryItems();
                        } else {
                            p.sendMessage(Component.text("           §6金钱不足。"));
                            p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                    }
                }
            });
            i.getAndIncrement();
        });
    }
}