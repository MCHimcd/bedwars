package mc.bedwars.menu;

import mc.bedwars.factory.ItemCreator;
import mc.bedwars.game.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

import static mc.bedwars.factory.Message.convertMsg;
import static mc.bedwars.factory.Message.rMsg;

public class MainMenu extends SlotMenu {
    public static final Map<Player, Boolean> up = new HashMap<>();    //玩家进入游戏后是否在上层
    public static final List<Player> prepared = new ArrayList<>();
    public static int start_tick = -1;
    public static boolean pre_start = false;

    public MainMenu(Player p) {
        super(27, Component.text("主菜单", NamedTextColor.GOLD), p);
        setSlot(11, ItemCreator.create(Material.PAPER).name(rMsg("<color:#FFF2E2>选择皮肤")).data(90000).getItem(), (it, pl) -> {
            pl.openInventory(new SkinMenu(pl, 1).getInventory());
        });

        setSlot(18, ItemCreator.create(Material.SCAFFOLDING)
                .name(rMsg("<color:#DCE2F1>决定在 上层还是下层 下棋"))
                .lore(Collections.singletonList(rMsg("当前设定：<gold>%s层".formatted(up.getOrDefault(p, true) ? "上" : "下"))))
                .getItem(), (it, pl) -> {
            var isUp = up.getOrDefault(pl, true);
            up.put(pl, !isUp);
            pl.playSound(pl, Sound.BLOCK_SCAFFOLDING_STEP, 1f, 1f);
        });

        setSlot(15, ItemCreator.create(Material.BOOK).name(rMsg("<color:#C7EDCC>新手必看")).data(0).getItem(), (it, pl) -> {
            convertMsg(List.of(
                    "<red>  *BEDWARS桌游版*",
                    " ",
                    "<click:open_url:https://space.bilibili.com/285815215?spm_id_from=333.1365.0.0><red>  新手教程<white><点击跳转>",
                    " ",
                    "<click:open_url:https://space.bilibili.com/285815215?spm_id_from=333.1365.0.0><dark_aqua>  地图制作：极熠工作室<white><点击跳转>",
                    "<click:open_url:https://space.bilibili.com/613586208><light_purple>  游戏设计：不知姓名の某人<white><点击跳转>"
            )).forEach(pl::sendMessage);
        });

        if (GameState.started) {
            setSlot(13, ItemCreator.create(Material.CRYING_OBSIDIAN)
                    .name(rMsg("<dark_red>游戏已开始"))
                    .getItem(), (it, pl) -> {
            });
        } else if (prepared.contains(p)) {
            setSlot(13, ItemCreator.create(Material.CRYING_OBSIDIAN)
                    .name(rMsg("<red>点击取消准备"))
                    .lore(List.of(rMsg("<gray>已准备人数：%d".formatted(prepared.size()))))
                    .getItem(), (it, pl) -> {
                // 取消准备
                if (!pre_start)
                    removePrepared(p);
            });
        } else {
            setSlot(13, ItemCreator.create(Material.OBSIDIAN)
                    .name(rMsg("<green>点击准备"))
                    .getItem(), (it, pl) -> {
                // 准备
                if (pre_start) {
                    pl.sendMessage(rMsg("<dark_red>游戏已开始"));
                } else if (prepared.size() <= 3) {
                    prepared.add(pl);
                    Bukkit.getOnlinePlayers().forEach(pl1 -> {
                        pl1.sendMessage(p.name().append(rMsg("<green>已准备")));
                        pl1.playSound(pl1, Sound.AMBIENT_UNDERWATER_ENTER, 1f, 1f);
                    });
                    if (prepared.size() > 1) start_tick = 0;
                } else player.sendMessage(rMsg("<red>人数已满"));
            });
        }

        // 管理员选项
        if (p.isOp()) {
            if (GameState.started)
                setSlot(26, ItemCreator.create(Material.BARRIER)
                        .name(rMsg("<dark_aqua>强制结束游戏"))
                        .getItem(), (it, pl) -> {
                    Bukkit.getOnlinePlayers().forEach(player1 -> {
                        player1.sendMessage(pl.name().append(rMsg("强制结束了游戏")));
                    });
                    GameState.reset();
                });
        }
    }

    public static void removePrepared(Player p) {
        if (prepared.remove(p)) {
            Bukkit.getOnlinePlayers().forEach(pl -> {
                pl.sendMessage(p.name().append(rMsg("<red>取消了准备")));
                pl.playSound(pl, Sound.AMBIENT_UNDERWATER_EXIT, 1f, 1f);
            });
            if (prepared.size() < 2) start_tick = -1;
        }
    }
}
