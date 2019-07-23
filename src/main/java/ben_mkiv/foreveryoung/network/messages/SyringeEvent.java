package ben_mkiv.foreveryoung.network.messages;

import ben_mkiv.foreveryoung.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SyringeEvent {
    protected int targetID = Integer.MIN_VALUE;

    protected String syringeType = "";

    public SyringeEvent() {}

    public SyringeEvent(@Nonnull Entity target, @Nonnull ItemStack syringeStack) {
        this.targetID = target.getEntityId();

        if(syringeStack.getItem().equals(ModItems.SyringeYouth))
            this.syringeType = "inject";
        else if(syringeStack.getItem().equals(ModItems.Syringe))
            this.syringeType = "drain";
    }

    public SyringeEvent(int entityId, String typeSyringe) {
        this.targetID = entityId;
        this.syringeType = typeSyringe;
    }

    public static void encode(SyringeEvent pkt, PacketBuffer buf){
        buf.writeInt(pkt.targetID);
        buf.writeBoolean(pkt.syringeType.equals("inject"));
    }

    public static SyringeEvent decode(PacketBuffer buf){
        int targetID = buf.readInt();
        String syringeType = buf.readBoolean() ? "inject" : "drain";

        return new SyringeEvent(targetID, syringeType);
    }

    public static class Handler {

        public static void handle(SyringeEvent message, Supplier<NetworkEvent.Context> ctx){
            if(ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
                return;
            }

            if(message.targetID != Integer.MIN_VALUE) {
                Entity target = Minecraft.getInstance().player.world.getEntityByID(message.targetID);

                if(target == null)
                    return;

                target.performHurtAnimation();

                if(message.syringeType.equals("inject")) {
                    target.getEntityWorld().addParticle(ParticleTypes.WITCH, target.posX + target.world.rand.nextFloat() * 0.5, target.posY + 0.5 + target.world.rand.nextFloat() * 0.5, target.posZ + target.world.rand.nextFloat() * 0.5, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D);
                }
                else {
                    target.getEntityWorld().addParticle(ParticleTypes.ANGRY_VILLAGER, target.posX + target.world.rand.nextFloat() * 0.5, target.posY + 0.5 + target.world.rand.nextFloat() * 0.5, target.posZ + target.world.rand.nextFloat() * 0.5, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D);
                }
            }



            return;
        }
    }

}
