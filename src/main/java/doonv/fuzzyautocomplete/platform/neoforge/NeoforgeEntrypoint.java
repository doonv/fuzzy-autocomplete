//? neoforge {
/*package doonv.fuzzyautocomplete.platform.neoforge;

import doonv.fuzzyautocomplete.FuzzyAutocomplete;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(FuzzyAutocomplete.MOD_ID)
public class NeoforgeEntrypoint {
    public NeoforgeEntrypoint(IEventBus modEventBus, ModContainer container) {
        FuzzyAutocomplete.onInitialize();

        container.registerExtensionPoint(IConfigScreenFactory.class, new IConfigScreenFactory() {
            @Override
            public Screen createScreen(ModContainer modContainer, Screen parent) {
                return doonv.fuzzyautocomplete.config.ClientConfig.HANDLER.generateGui()
                        .generateScreen(parent);
            }
        });
    }
}
 *///?}
