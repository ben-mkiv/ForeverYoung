package ben_mkiv.foreveryoung.items;

import ben_mkiv.foreveryoung.Foreveryoung;
import ben_mkiv.foreveryoung.network.ForeverYoungNetwork;
import ben_mkiv.foreveryoung.network.messages.ItemUseEvent;
import ben_mkiv.foreveryoung.network.messages.SyringeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Syringe extends Item {
    private static DamageSource damageSource = new DamageSource("foreveryoung.syringe");

    public Syringe() {
        super();
        setCreativeTab(Foreveryoung.CreativeTab);
        setUnlocalizedName("syringe");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }



    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        return EnumActionResult.FAIL;
    }

    @SideOnly(Side.CLIENT)
    public static boolean onItemUse(EntityPlayer player, Entity target){
        if(player == null || target == null)
            return false;

        ForeverYoungNetwork.channel.sendToServer(new ItemUseEvent(player));

        return true;
    }

    public void execute(ItemStack stack, EntityPlayer player, Entity target){
        if(target == null)
            return;

        if(!(target instanceof EntityAgeable)) {
            player.sendStatusMessage(new TextComponentString("'" + target.getDisplayName().getUnformattedText() + "' is not supported"), true);
            return;
        }

        ItemStack newStack = ItemStack.EMPTY;

        if(player.getHeldItemMainhand().getItem().equals(Foreveryoung.Items.Syringe))
            newStack = processDrainInteraction(player, player.getHeldItemMainhand(), (EntityAgeable) target);

        if(player.getHeldItemMainhand().getItem().equals(Foreveryoung.Items.SyringeYouth))
            newStack = processInjectInteraction(player, player.getHeldItemMainhand(), (EntityAgeable) target);

        if(!newStack.isEmpty() && !newStack.equals(player.getHeldItemMainhand())) {
            player.getHeldItemMainhand().shrink(1);

            if (player.getHeldItemMainhand().isEmpty())
                player.setHeldItem(EnumHand.MAIN_HAND, newStack);
            else if (!player.inventory.addItemStackToInventory(newStack))
                player.dropItem(newStack, false);

            target.attackEntityFrom(damageSource, 0.5f);
        }
    }

    public static ItemStack processDrainInteraction(EntityPlayer player, ItemStack stackIn, EntityAgeable target){
        if(!target.isChild()) {
            player.sendStatusMessage(new TextComponentString("'" + target.getDisplayName().getUnformattedText() + "' is mature"), true);
            return stackIn;
        }

        ForeverYoungNetwork.sendToTrackingPlayers(new SyringeEvent(target, stackIn), target);

        return new ItemStack(Foreveryoung.Items.SyringeBlood);
    }

    public static ItemStack processInjectInteraction(EntityPlayer player, ItemStack stackIn, EntityAgeable target){
        if(!target.isChild()) {
            player.sendStatusMessage(new TextComponentString("'" + target.getDisplayName().getUnformattedText() + "' is mature"), true);
            return stackIn;
        }

        target.setGrowingAge(Integer.MIN_VALUE);

        player.sendStatusMessage(new TextComponentString("'" + target.getDisplayName().getUnformattedText() + "' will live a long life."), true);

        if(!target.hasCustomName()){
            //player.sendStatusMessage(new TextComponentString("you should put a nametag on this mob if you dont want it to despawn"), false);
        }

        ForeverYoungNetwork.sendToTrackingPlayers(new SyringeEvent(target, stackIn), target);

        return new ItemStack(Foreveryoung.Items.Syringe);
    }
}
