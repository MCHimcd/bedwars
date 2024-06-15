package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.card.Blocks.*;
import mc.bedwars.game.card.boost.*;
import mc.bedwars.game.card.equips.*;
import mc.bedwars.game.card.props.*;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static mc.bedwars.game.GameState.players_data;

public class ShopMenu extends SlotMenu {

    public ShopMenu(Player p) {
        super(27, Component.text("商店"), p);
        Map<Card, ItemStack> items = new HashMap<>() {{
            put(new EndStone(), ItemCreator.create(Material.PAPER).data(40002).name(Component.text("§c§l末地石")).getItem());
            put(new ExplosionproofGlass(), ItemCreator.create(Material.PAPER).data(40004).name(Component.text("§c§l防爆玻璃")).getItem());
            put(new Obsidian(), ItemCreator.create(Material.PAPER).data(40005).name(Component.text("§c§l黑曜石")).getItem());
            put(new Plank(), ItemCreator.create(Material.PAPER).data(40001).name(Component.text("§c§l木板")).getItem());
            put(new Wool(), ItemCreator.create(Material.PAPER).data(40003).name(Component.text("§c§l羊毛")).getItem());
            put(new HealingSpring(), ItemCreator.create(Material.PAPER).data(30003).name(Component.text("§c§l生命泉水")).getItem());
            put(new Protection(), ItemCreator.create(Material.PAPER).data(30002).name(Component.text("§c§l保护")).getItem());
            put(new Sharp(), ItemCreator.create(Material.PAPER).data(30001).name(Component.text("§c§l锋利")).getItem());
            put(new DiamondEquips(), ItemCreator.create(Material.PAPER).data(10007).name(Component.text("§c§l钻石装备")).getItem());
            put(new DiamondSword(), ItemCreator.create(Material.PAPER).data(10006).name(Component.text("§c§l钻石剑")).getItem());
            put(new IronAxe(), ItemCreator.create(Material.PAPER).data(10005).name(Component.text("§c§l铁斧")).getItem());
            put(new IronEquips(), ItemCreator.create(Material.PAPER).data(10002).name(Component.text("§c§l铁装备")).getItem());
            put(new IronSword(), ItemCreator.create(Material.PAPER).data(10001).name(Component.text("§c§l铁剑")).getItem());
            put(new Pickaxe(), ItemCreator.create(Material.PAPER).data(10008).name(Component.text("§c§l镐子")).getItem());
            put(new Scissors(), ItemCreator.create(Material.PAPER).data(10004).name(Component.text("§c§l剪刀")).getItem());
            put(new BridgeEgg(), ItemCreator.create(Material.PAPER).data(20002).name(Component.text("§c§l搭桥蛋")).getItem());
            put(new EnderPearl(), ItemCreator.create(Material.PAPER).data(20003).name(Component.text("§c§l末影之眼")).getItem());
            put(new Fireball(), ItemCreator.create(Material.PAPER).data(20004).name(Component.text("§c§l火球")).getItem());
            put(new GoldenApple(), ItemCreator.create(Material.PAPER).data(20007).name(Component.text("§c§l金苹果")).getItem());
            put(new KbtStick(), ItemCreator.create(Material.PAPER).data(10003).name(Component.text("§c§l击退棒")).getItem());
            put(new PotionofInvisibility(), ItemCreator.create(Material.PAPER).data(20008).name(Component.text("§c§l隐身药水")).getItem());
            put(new PotionofLeaping(), ItemCreator.create(Material.PAPER).data(20005).name(Component.text("§c§l跳跃药水")).getItem());
            put(new PotionofSwiftness(), ItemCreator.create(Material.PAPER).data(20006).name(Component.text("§c§l迅捷药水")).getItem());
            put(new Tnt(), ItemCreator.create(Material.PAPER).name(Component.text("TNT")).data(20001).getItem());
        }};
        AtomicInteger i = new AtomicInteger();
        items.forEach((card, itemStack) -> {
            setSlot(i.get(), itemStack, (it, pl) -> {
                var pd = players_data.get(pl);
                if (pd.items.stream().filter(c -> c.Name().equals(card.Name())).count() >= card.itemMaxCount()) {
                    p.sendMessage(Component.text("<S>      §a持有%s超过上限。".formatted(card.Name())));
                    p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                } else if (pd.removeMoney(card.costMoney())) {
                    if (card instanceof Equip) {
                        pd.equipments.add(card);
                    } else pd.items.add(card);
                    p.playSound(p, Sound.ENTITY_VILLAGER_YES, 1f, 1f);
                } else {
                    p.sendMessage(Component.text("<S>      §6金钱不足。"));
                    p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                }
            });
            i.getAndIncrement();
        });
    }
}