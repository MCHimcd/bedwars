package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static mc.bedwars.factory.Message.rMsg;

public class SkinMenu extends SlotMenu {
    public static final Map<Player, Integer> skins = new HashMap<>();
    public static final List<Component> itemNames = List.of(
            rMsg("<aqua>BW玩家"),
            rMsg("<dark_aqua>蓝空星耀"),
            rMsg("<dark_aqua>归仙若尘"),
            rMsg("<dark_aqua>重铜龙蝼"),
            rMsg("<dark_aqua>之翼"),
            rMsg("<dark_aqua>艾萨克.莫德拉尔"),
            rMsg("<dark_aqua>幽冥"),
            rMsg("<dark_aqua>茗盛"),
            rMsg("<dark_aqua>幽莲"),
            rMsg("<dark_aqua>天瑜琳"),
            rMsg("<dark_aqua>希葛"),
            rMsg("<dark_aqua>夏念"),
            rMsg("<dark_aqua>芙安涅姆"),
            rMsg("<dark_aqua>落星辰"),
            rMsg("<dark_aqua>海港玫瑰"),
            rMsg("<dark_aqua>红雷"),
            rMsg("<dark_aqua>遗民"),
            rMsg("<light_purple>某人"),
            rMsg("18"),
            rMsg("19"),
            rMsg("20"),
            rMsg("21"),
            rMsg("22"),
            rMsg("23"),
            rMsg("24"),
            rMsg("25"),
            rMsg("26"),
            rMsg("27"),
            rMsg("28"),
            rMsg("29"),
            rMsg("30")
    );

    public SkinMenu(Player p, int page) {
        super(27, Component.text("选择皮肤-第%d页".formatted(page), NamedTextColor.DARK_AQUA), p);
        setPage(page);
    }

    private void setPage(int page) {
        var start = 0;
        if (page == 1) {
//            setSlot(26, ItemCreator.create(Material.PAPER).name(Component.text("下一页")).data(99999).getItem(), (it, pl) -> {
//                pl.openInventory(new SkinMenu(pl, page + 1).getInventory());
//                pl.playSound(pl, Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
//            });
        } else if (page >= 2) {
            start = 1;
            setSlot(0, ItemCreator.create(Material.PAPER).name(Component.text("上一页")).data(99998).getItem(), (it, pl) -> {
                pl.openInventory(new SkinMenu(pl, page - 1).getInventory());
                pl.playSound(pl, Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
            });
        }
//        IntStream.rangeClosed(start, start + 25).forEach(i -> {
        IntStream.rangeClosed(0, 17).forEach(i -> {
            var name = itemNames.get(i + (page - 1) * 25);
            var data = i + 90000 + (page - 1) * 25;
            setSlot(i, ItemCreator.create(Material.PAPER).data(data).name(name).getItem(), (it, pl) -> {
                skins.put(pl, data);
                pl.sendMessage(rMsg("你已选择").append(name));
                pl.playSound(pl, Sound.ITEM_ARMOR_EQUIP_CHAIN, 1f, 1f);
            });
        });
    }
}
