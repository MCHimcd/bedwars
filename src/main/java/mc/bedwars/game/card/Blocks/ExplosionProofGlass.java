package mc.bedwars.game.card.Blocks;

import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ExplosionProofGlass extends Card implements isBlock {
    @Override
    public boolean effect(Player player) {
        return true;
    }

    public Material material() {
        return Material.BLACK_STAINED_GLASS;
    }

    @Override
    public int power() {
        return 0;
    }

    @Override
    public int costMoney() {
        return 16;
    }

    @Override
    public int itemMaxCount() {
        return 4;
    }

    @Override
    public int CustomModelData() {
        return 40004;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return Component.text("防爆玻璃");
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("方块,可以防爆");
    }
}