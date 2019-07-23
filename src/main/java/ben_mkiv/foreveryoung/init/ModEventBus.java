package ben_mkiv.foreveryoung.init;

import ben_mkiv.foreveryoung.Foreveryoung;
import ben_mkiv.foreveryoung.items.Syringe;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegistryEvent;

@Mod.EventBusSubscriber(modid = Foreveryoung.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerInteractEvent(PlayerInteractEvent.EntityInteractSpecific event){
        if(!(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof Syringe))
            return;

        Entity target = event.getTarget();

        if(target == null)
            return;

        event.setResult(Event.Result.DENY);
        if(event.isCancelable())
            event.setCanceled(true);

        if(!event.getHand().equals(Hand.MAIN_HAND)) return;
        if(!event.getSide().isClient()) return;

        Syringe.onItemUse(event.getEntityPlayer(), target);
    }

    @SubscribeEvent
    public static void addItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ModItems.Syringe.setRegistryName(Foreveryoung.MOD_ID, "syringe"));
        event.getRegistry().register(ModItems.SyringeYouth.setRegistryName(Foreveryoung.MOD_ID, "syringe_youth"));
        event.getRegistry().register(ModItems.SyringeBlood.setRegistryName(Foreveryoung.MOD_ID, "syringe_blood"));
    }

}
