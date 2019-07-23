package ben_mkiv.foreveryoung;

import ben_mkiv.foreveryoung.items.*;
import ben_mkiv.foreveryoung.network.ForeverYoungNetwork;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
        modid = Foreveryoung.MOD_ID,
        name = Foreveryoung.MOD_NAME,
        version = Foreveryoung.VERSION
)
public class Foreveryoung {

    public static final String MOD_ID = "foreveryoung";
    public static final String MOD_NAME = "ForeverYoung";
    public static final String VERSION = "1.0";

    @Mod.Instance(MOD_ID)
    public static Foreveryoung INSTANCE;

    public static CreativeTabs CreativeTab = new CreativeTabs("ForeverYoung") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.SyringeYouth);
        }
    };

    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
        public static final Syringe Syringe = new Syringe();
        public static final SyringeYouth SyringeYouth = new SyringeYouth();
        public static final SyringeBlood SyringeBlood = new SyringeBlood();
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        //Mod Network
        ForeverYoungNetwork.init();
    }

    @Mod.EventBusSubscriber
    public static class PlayerInteractionEventHandler {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        @SideOnly(Side.CLIENT)
        public static void onPlayerInteractEvent(PlayerInteractEvent.EntityInteract event){
            if(!(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof Syringe))
                return;

            Entity target = event.getTarget();

            if(target == null)
                return;

            event.setResult(Event.Result.DENY);
            if(event.isCancelable())
                event.setCanceled(true);

            if(!event.getHand().equals(EnumHand.MAIN_HAND)) return;
            if(!event.getSide().isClient()) return;

            Syringe.onItemUse(event.getEntityPlayer(), target);
        }
    }

    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(Items.Syringe.setRegistryName(MOD_ID, "syringe"));
            event.getRegistry().register(Items.SyringeYouth.setRegistryName(MOD_ID, "syringe_youth"));
            event.getRegistry().register(Items.SyringeBlood.setRegistryName(MOD_ID, "syringe_blood"));
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onRegisterModels(ModelRegistryEvent event) {
            registerItemModel(Items.Syringe, "syringe");
            registerItemModel(Items.SyringeYouth, "syringe_youth");
            registerItemModel(Items.SyringeBlood, "syringe_blood");
        }

        @SideOnly(Side.CLIENT)
        private static void registerItemModel(final Item item, final String itemName) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(MOD_ID + ":" + itemName.toLowerCase(), "inventory"));
        }

    }

}
