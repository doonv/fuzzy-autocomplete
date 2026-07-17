//? neoforge {
/*package doonv.fuzzyautocomplete.platform.neoforge;

import doonv.fuzzyautocomplete.FuzzyAutocomplete;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(Dist.CLIENT)
public class NeoforgeClientEventSubscriber {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        FuzzyAutocomplete.onInitializeClient();
    }
}
 *///?}
