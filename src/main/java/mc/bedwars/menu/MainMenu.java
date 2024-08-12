package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static mc.bedwars.factory.Message.rMsg;

public class MainMenu extends SlotMenu {
    public static final List<Player> prepared = new ArrayList<>();
    public static int start_tick = -1;

    public MainMenu(Player p) {
        super(27, Component.text("主菜单", NamedTextColor.GOLD), p);

        setSlot(11, ItemCreator.create(Material.PAPER).name(rMsg("选择皮肤")).data(99997).getItem(), (it, pl) -> {
            pl.openInventory(new SkinMenu(pl, 1).getInventory());
        });

        if (prepared.contains(p)) {
            setSlot(15, ItemCreator.create(Material.CRYING_OBSIDIAN)
                    .name(rMsg("点击取消准备", NamedTextColor.RED))
                    .lore(List.of(rMsg("已准备人数：%d".formatted(prepared.size()), NamedTextColor.GRAY)))
                    .getItem(), (it, pl) -> {
                // 取消准备
                removePrepared(p);
            });
        } else {
            setSlot(15, ItemCreator.create(Material.OBSIDIAN)
                    .name(rMsg("点击准备", NamedTextColor.GREEN))
                    .getItem(), (it, pl) -> {
                // 准备
                if (prepared.size() <= 3) {
                    prepared.add(pl);
                    Bukkit.getOnlinePlayers().forEach(pl1 -> {
                        pl1.sendMessage(p.name().append(rMsg("已准备")));
                        pl1.playSound(pl1, Sound.AMBIENT_UNDERWATER_ENTER, 1f, 1f);
                    });
                    if (prepared.size() > 1) start_tick = 0;
                } else player.sendMessage(rMsg("人数已满", NamedTextColor.RED));
            });
        }

        // 管理员选项
//        if (p.isOp()) {
//
//        }
    }

    public static void removePrepared(Player p) {
        if (prepared.remove(p)) {
            Bukkit.getOnlinePlayers().forEach(pl -> {
                pl.sendMessage(p.name().append(rMsg("取消了准备")));
                pl.playSound(pl, Sound.AMBIENT_UNDERWATER_EXIT, 1f, 1f);
            });
            if (prepared.size() < 2) start_tick = -1;
        }
    }
}
