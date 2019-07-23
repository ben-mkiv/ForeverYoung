package ben_mkiv.foreveryoung.network.messages;

import ben_mkiv.foreveryoung.Foreveryoung;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;

public class SyringeEvent implements IMessage {
    protected int targetID = Integer.MIN_VALUE;

    String syringeType = "";

    public SyringeEvent() {}

    public SyringeEvent(@Nonnull Entity target, @Nonnull ItemStack syringeStack) {
        this.targetID = target.getEntityId();

        if(syringeStack.getItem().equals(Foreveryoung.Items.SyringeYouth))
            this.syringeType = "inject";
        else if(syringeStack.getItem().equals(Foreveryoung.Items.Syringe))
            this.syringeType = "drain";
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.targetID = buf.readInt();
        this.syringeType = buf.readBoolean() ? "inject" : "drain";
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.targetID);
        buf.writeBoolean(syringeType.equals("inject"));
    }

    public static class Handler implements IMessageHandler<SyringeEvent, IMessage> {

        @Override
        public IMessage onMessage(SyringeEvent message, MessageContext ctx) {
            if(message.targetID != Integer.MIN_VALUE) {
                Entity target = Minecraft.getMinecraft().player.world.getEntityByID(message.targetID);

                if(target == null)
                    return null;

                target.performHurtAnimation();

                if(message.syringeType.equals("inject"))
                    target.getEntityWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, target.posX + target.world.rand.nextFloat() * 0.5, target.posY + 0.5 + target.world.rand.nextFloat() * 0.5, target.posZ + target.world.rand.nextFloat() * 0.5, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D);
                else
                    target.getEntityWorld().spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, target.posX + target.world.rand.nextFloat() * 0.5, target.posY + 0.5 + target.world.rand.nextFloat() * 0.5, target.posZ + target.world.rand.nextFloat() * 0.5, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D, target.world.rand.nextGaussian() * 0.02D);
            }



            return null;
        }
    }

}
