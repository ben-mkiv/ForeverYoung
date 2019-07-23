package ben_mkiv.foreveryoung.network.messages;

import ben_mkiv.foreveryoung.items.Syringe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;


public class ItemUseEvent {
    protected int targetID = Integer.MIN_VALUE;

    public ItemUseEvent() {}

    public ItemUseEvent(@Nonnull Entity target) {
        this.targetID = target.getEntityId();
    }

    public ItemUseEvent(int entityId) {
        this.targetID = entityId;
    }


    public static void encode(ItemUseEvent pkt, PacketBuffer buf){
        buf.writeInt(pkt.targetID);
    }

    public static ItemUseEvent decode(PacketBuffer buf){
        return new ItemUseEvent(buf.readInt());
    }

    public static class Handler{
        public static void handle(ItemUseEvent message, Supplier<NetworkEvent.Context> ctx) {
            if(ctx.get().getDirection() != NetworkDirection.PLAY_TO_SERVER) {
                return;
            }

            World world = ctx.get().getSender().world;

            PlayerEntity player = ctx.get().getSender();

            if (player == null)
                return;

            ItemStack stackUsed = player.getHeldItemMainhand();

            Entity target = null;

            if(message.targetID != Integer.MIN_VALUE)
                target = world.getEntityByID(message.targetID);


            if(stackUsed.getItem() instanceof Syringe)
                ((Syringe) stackUsed.getItem()).execute(stackUsed, player, target);
        }
    }

}