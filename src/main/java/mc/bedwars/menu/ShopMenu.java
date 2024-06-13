package mc.bedwars.menu;

import mc.bedwars.game.card.Blocks.EndStone;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static mc.bedwars.game.GameState.players_data;

public class ShopMenu extends SlotMenu {

    public ShopMenu(Player p) {
        super(27, Component.text("商店"), p);
        Map<Card, ItemStack> items = new HashMap<>() {{
            put(new EndStone(), new ItemStack(Material.END_STONE));
        }};
        AtomicInteger i = new AtomicInteger();
        items.forEach((card, itemStack) -> {
            setSlot(i.get(), itemStack, (it, pl) -> {
                var pd = players_data.get(pl);
                if (pd.items.stream().filter(c -> c.Name().equals(card.Name())).count() >= card.itemMaxCount()){
                    p.sendMessage(Component.text("<S>      §a持有%s超过上限。".formatted(card.Name())));
                    p.playSound(p, Sound.ENTITY_VILLAGER_NO,1f,1f);
                } else if( pd.removeMoney(card.costMoney())) {
                    pd.items.add(card);
                    p.playSound(p, Sound.ENTITY_VILLAGER_YES,1f,1f);
                } else {
                    p.sendMessage(Component.text("<S>      §6金钱不足。"));
                    p.playSound(p, Sound.ENTITY_VILLAGER_NO,1f,1f);
                }
            });
            i.getAndIncrement();
        });
    }
}