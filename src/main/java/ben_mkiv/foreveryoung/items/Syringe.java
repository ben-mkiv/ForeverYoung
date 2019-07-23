package ben_mkiv.foreveryoung.items;

import ben_mkiv.foreveryoung.Foreveryoung;
import ben_mkiv.foreveryoung.init.ModItems;
import ben_mkiv.foreveryoung.network.ForeverYoungNetwork;
import ben_mkiv.foreveryoung.network.messages.ItemUseEvent;
import ben_mkiv.foreveryoung.network.messages.SyringeEvent;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;

public class Syringe extends Item {
    private static DamageSource damageSource = new DamageSource("foreveryoung.syringe");

    public Syringe() {
        super(new Properties().group(Foreveryoung.group));
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean onItemUse(PlayerEntity player, Entity target){
        if(player == null || target == null)
            return false;

        Foreveryoung.channel.sendToServer(new ItemUseEvent(target));

        return true;
    }

    public void execute(ItemStack stack, PlayerEntity player, Entity target){
        if(target == null)
            return;

        if(!(target instanceof AgeableEntity)) {
            player.sendStatusMessage(new StringTextComponent("'" + target.getDisplayName().getFormattedText() + "' is not supported"), true);
            return;
        }

        ItemStack newStack = ItemStack.EMPTY;

        if(player.getHeldItemMainhand().getItem().equals(ModItems.Syringe))
            newStack = processDrainInteraction(player, player.getHeldItemMainhand(), (AgeableEntity) target);

        if(player.getHeldItemMainhand().getItem().equals(ModItems.SyringeYouth))
            newStack = processInjectInteraction(player, player.getHeldItemMainhand(), (AgeableEntity) target);

        if(!newStack.isEmpty() && !newStack.equals(player.getHeldItemMainhand())) {
            player.getHeldItemMainhand().shrink(1);

            if (player.getHeldItemMainhand().isEmpty())
                player.setHeldItem(Hand.MAIN_HAND, newStack);
            else if (!player.inventory.addItemStackToInventory(newStack))
                player.dropItem(newStack, false);

            target.attackEntityFrom(damageSource, 0.5f);
        }
    }

    public static ItemStack processDrainInteraction(PlayerEntity player, ItemStack stackIn, AgeableEntity target){
        if(!target.isChild()) {
            player.sendStatusMessage(new StringTextComponent("'" + target.getDisplayName().getFormattedText() + "' is mature"), true);
            return stackIn;
        }

        for(PlayerEntity playerInRange : ForeverYoungNetwork.getPlayersInRange(target, 32)){
            Foreveryoung.channel.sendTo(new SyringeEvent(target, stackIn), ((ServerPlayerEntity) playerInRange).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }

        return new ItemStack(ModItems.SyringeBlood);
    }

    public static ItemStack processInjectInteraction(PlayerEntity player, ItemStack stackIn, AgeableEntity target){
        if(!target.isChild()) {
            player.sendStatusMessage(new StringTextComponent("'" + target.getDisplayName().getFormattedText() + "' is mature"), true);
            return stackIn;
        }

        target.setGrowingAge(Integer.MIN_VALUE);

        player.sendStatusMessage(new StringTextComponent("'" + target.getDisplayName().getFormattedText() + "' will live a long life."), true);

        if(!target.hasCustomName()){
            //player.sendStatusMessage(new TextComponentString("you should put a nametag on this mob if you dont want it to despawn"), false);
        }

        for(PlayerEntity playerInRange : ForeverYoungNetwork.getPlayersInRange(target, 32)){
            Foreveryoung.channel.sendTo(new SyringeEvent(target, stackIn), ((ServerPlayerEntity) playerInRange).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }

        return new ItemStack(ModItems.Syringe);
    }
}
