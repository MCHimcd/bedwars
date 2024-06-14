package mc.bedwars.game.card.equips;
import mc.bedwars.game.card.Card;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class IronSword extends Card implements isEquip{
    @Override
    public void effect(Player player) {

    }
    public boolean isEquip(){return true;}
    @Override
    public int power() {
        return 2;
    }
    @Override
    public int costMoney() {
        return 4;
    }
    @Override
    public int itemMaxCount() {
        return 1;
    }
    @Override
    public int CustomModelData() {
        return 10001;
    }
    @Override
    public boolean CanDrop(){
        return true;
    }
    @Override
    public Component Name() {
        return Component.text("铁剑");
    }
    @Override
    public boolean CanUse(){
        return false;
    }
    @Override
    public Component Introduction() {
        return Component.text("获得2点战力");
    }
}