package ben_mkiv.foreveryoung.network.messages;

import ben_mkiv.foreveryoung.items.Syringe;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUseEvent implements IMessage {
    protected int pentid, dimId, targetID = Integer.MIN_VALUE;

    public ItemUseEvent() {}

    @SideOnly(Side.CLIENT)
    public ItemUseEvent(EntityPlayer player) {
        this(player, Minecraft.getMinecraft().objectMouseOver.entityHit);
    }

    public ItemUseEvent(EntityPlayer player, Entity target) {
        this.pentid = player.getEntityId();
        this.dimId = player.world.provider.getDimension();

        if(target != null)
            this.targetID = target.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pentid = buf.readInt();
        this.dimId = buf.readInt();
        this.targetID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pentid);
        buf.writeInt(this.dimId);
        buf.writeInt(this.targetID);
    }

    public static class Handler implements IMessageHandler<ItemUseEvent, IMessage> {

        @Override
        public IMessage onMessage(ItemUseEvent message, MessageContext ctx) {
            World world = DimensionManager.getWorld(message.dimId);

            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.pentid);

            if (player == null)
                return null;

            ItemStack stackUsed = player.getHeldItemMainhand();

            Entity target = null;

            if(message.targetID != Integer.MIN_VALUE)
                target = world.getEntityByID(message.targetID);


            if(stackUsed.getItem() instanceof Syringe)
                ((Syringe) stackUsed.getItem()).execute(stackUsed, player, target);

            return null;
        }
    }

}