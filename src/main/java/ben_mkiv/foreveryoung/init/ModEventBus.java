package ben_mkiv.foreveryoung.init;

import ben_mkiv.foreveryoung.Foreveryoung;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegistryEvent;

@Mod.EventBusSubscriber(modid = Foreveryoung.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {
    @SubscribeEvent
    public static void addItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ModItems.Syringe.setRegistryName(Foreveryoung.MOD_ID, "syringe"));
        event.getRegistry().register(ModItems.SyringeYouth.setRegistryName(Foreveryoung.MOD_ID, "syringe_youth"));
        event.getRegistry().register(ModItems.SyringeBlood.setRegistryName(Foreveryoung.MOD_ID, "syringe_blood"));
    }
}
