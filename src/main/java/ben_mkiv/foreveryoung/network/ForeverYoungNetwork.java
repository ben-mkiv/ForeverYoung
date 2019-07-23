package ben_mkiv.foreveryoung.network;

import ben_mkiv.foreveryoung.Foreveryoung;
import ben_mkiv.foreveryoung.network.messages.*;

public class ForeverYoungNetwork {

    public static void init(){
        int id=-1;

        Foreveryoung.channel.registerMessage(id++, ItemUseEvent.class, ItemUseEvent::encode, ItemUseEvent::decode, ItemUseEvent.Handler::handle);
        Foreveryoung.channel.registerMessage(id++, SyringeEvent.class, SyringeEvent::encode, SyringeEvent::decode, SyringeEvent.Handler::handle);
    }


}