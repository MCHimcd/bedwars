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
            rMsg("<aqua>原皮"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("5"),
            rMsg("6"),
            rMsg("7"),
            rMsg("8"),
            rMsg("9"),
            rMsg("10"),
            rMsg("11"),
            rMsg("12"),
            rMsg("13"),
            rMsg("14"),
            rMsg("15"),
            rMsg("16"),
            rMsg("17"),
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
            rMsg("30"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4"),
            rMsg("1"),
            rMsg("2"),
            rMsg("3"),
            rMsg("4")
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
