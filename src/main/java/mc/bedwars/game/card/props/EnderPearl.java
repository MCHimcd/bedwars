package mc.bedwars.game.card.props;

import mc.bedwars.game.GameState;
import mc.bedwars.game.card.Card;
import mc.bedwars.game.map.GameMap;
import mc.bedwars.game.map.node.island.resource.Bed;
import net.kyori.adventure.text.Component;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static mc.bedwars.game.GameState.map;

public class EnderPearl extends Card implements Prop {
    @Override
    public boolean effect(Player player) {
        var pd = GameState.players_data.get(player);
        pd.addAction(1);
        pd.location.players.remove(player);
        pd.location = pd.target_location;
        map.moveTo(player, pd.target_location);
        player.getWorld().spawnParticle(Particle.WITCH,pd.getMarker().getLocation(),100,0.5,1,0.5,0.3,null,true);
        var dxz=pd.getOrder()<=2?(pd.getOrder()-3)*0.5:(pd.getOrder()-2)*0.5;
        pd.getMarker().teleport(GameMap.getLocation(pd.target_location).add(dxz,0,dxz));
        player.getWorld().spawnParticle(Particle.WITCH,pd.getMarker().getLocation(),100,0.5,1,0.5,0.3,null,true);
        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW,1f,1f);
        player.getWorld().sendMessage(Component.text("                 §l%s使用了 §7末影之眼".formatted(player.getName())));
        return true;
    }

    public void backHome(Player player){
        var pd = GameState.players_data.get(player);
        map.islands.stream().filter(island -> island instanceof Bed b && b.getOrder()==pd.getOrder()).findFirst().ifPresent(island -> {
            pd.location=island;
            var dxz=pd.getOrder()<=2?(pd.getOrder()-3)*0.5:(pd.getOrder()-2)*0.5;
            pd.getMarker().teleport(GameMap.getLocation(island).add(dxz,0,dxz));
            pd.items.remove(this);
            pd.resetInventoryItems();
        });
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
        return 1;
    }

    @Override
    public int CustomModelData() {
        return 20003;
    }

    @Override
    public boolean CanDrop() {
        return true;
    }

    @Override
    public Component Name() {
        return Component.text("末影珍珠");
    }

    @Override
    public boolean CanUse() {
        return true;
    }

    @Override
    public Component Introduction() {
        return Component.text("当被打入虚空时可以使用，回到相邻岛屿");
    }
}