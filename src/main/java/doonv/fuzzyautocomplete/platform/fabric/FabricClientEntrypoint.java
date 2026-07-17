//? fabric {
package doonv.fuzzyautocomplete.platform.fabric;

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import doonv.fuzzyautocomplete.FuzzyAutocomplete;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		if (!FabricMissingDepsScreen.YACL_LOADED) {
			ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
				if (screen instanceof TitleScreen)
					//~ if >=1.21.9 'forceSetScreen' -> 'setScreenAndShow'
					client.setScreenAndShow(new FabricMissingDepsScreen());
			});
			return; // Cancel rest of mod load to prevent crash
		}


		FuzzyAutocomplete.onInitializeClient();
	}
}
//?}
