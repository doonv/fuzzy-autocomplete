package doonv.fuzzyautocomplete.platform;

import java.nio.file.Path;

public interface Platform {
	boolean isModLoaded(String modId);

	ModLoader loader();

	String mcVersion();

	boolean isDevelopmentEnvironment();

	Path getConfigDir();

	enum ModLoader {FABRIC, NEOFORGE}
}
