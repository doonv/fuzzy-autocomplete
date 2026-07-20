//? fabric {
package doonv.fuzzyautocomplete.platform.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import doonv.fuzzyautocomplete.config.ClientConfig;

@Entrypoint("modmenu")
public class FabricModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parentScreen -> ClientConfig.HANDLER.generateGui()
				.generateScreen(parentScreen);
	}
}
//?}
