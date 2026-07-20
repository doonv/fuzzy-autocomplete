package doonv.fuzzyautocomplete;

import doonv.fuzzyautocomplete.platform.Platform;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuzzyAutocomplete {
	public static final String MOD_ID = /*$ mod_id*/ "fuzzyautocomplete";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();

	public static Platform platform() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new doonv.fuzzyautocomplete.platform.fabric.FabricPlatform();
		//?} neoforge {
		/*return new doonv.fuzzyautocomplete.platform.neoforge.NeoforgePlatform();
		 *///?}
	}

	public static void onInitialize() {
	}

	public static void onInitializeClient() {
		doonv.fuzzyautocomplete.config.ClientConfig.HANDLER.load();
	}

	public static Identifier path(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
