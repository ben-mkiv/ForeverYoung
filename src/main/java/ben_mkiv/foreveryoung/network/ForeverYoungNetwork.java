package ben_mkiv.foreveryoung.network;

import ben_mkiv.foreveryoung.Foreveryoung;
import ben_mkiv.foreveryoung.network.messages.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class ForeverYoungNetwork {

    public static void init(){
        int id=-1;

        Foreveryoung.channel.registerMessage(id++, ItemUseEvent.class, ItemUseEvent::encode, ItemUseEvent::decode, ItemUseEvent.Handler::handle);
        Foreveryoung.channel.registerMessage(id++, SyringeEvent.class, SyringeEvent::encode, SyringeEvent::decode, SyringeEvent.Handler::handle);
    }

    public static List<PlayerEntity> getPlayersInRange(Entity entity, int range){
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(-range, -range, -range, range, range, range).offset(entity.getPositionVec());

        return entity.getEntityWorld().getEntitiesWithinAABB(PlayerEntity.class, axisAlignedBB);
    }

}