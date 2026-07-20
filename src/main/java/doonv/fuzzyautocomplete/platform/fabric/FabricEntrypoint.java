//? fabric {
package doonv.fuzzyautocomplete.platform.fabric;

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import doonv.fuzzyautocomplete.FuzzyAutocomplete;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {
	@Override
	public void onInitialize() {
		FuzzyAutocomplete.onInitialize();
	}
}
//?}
