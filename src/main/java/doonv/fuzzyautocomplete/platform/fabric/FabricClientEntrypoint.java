//? fabric {
package doonv.fuzzyautocomplete.platform.fabric;

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import doonv.fuzzyautocomplete.FuzzyAutocomplete;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FuzzyAutocomplete.onInitializeClient();
	}
}
//?}
