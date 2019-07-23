package ben_mkiv.foreveryoung;

import ben_mkiv.foreveryoung.init.ModItems;
import ben_mkiv.foreveryoung.items.*;
import ben_mkiv.foreveryoung.client.ClientLifecycleHandler;
import ben_mkiv.foreveryoung.network.ForeverYoungNetwork;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraft.util.ResourceLocation;

@Mod.EventBusSubscriber(modid = Foreveryoung.MOD_ID)
@Mod(value = Foreveryoung.MOD_ID)
public class Foreveryoung {

    public static final String MOD_ID = "foreveryoung";
    public static final String MOD_NAME = "ForeverYoung";
    public static final String VERSION = "1.0";

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MOD_ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public Foreveryoung() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus()
                .<FMLClientSetupEvent>addListener(e -> new ClientLifecycleHandler().clientSetup(e));
    }

    public static ItemGroup group = new ItemGroup("ForeverYoung") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.SyringeYouth);
        }
    };

    private void setup(final FMLCommonSetupEvent event){
        //Mod Network
        ForeverYoungNetwork.init();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerInteractEvent(PlayerInteractEvent.EntityInteract event){
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



}
