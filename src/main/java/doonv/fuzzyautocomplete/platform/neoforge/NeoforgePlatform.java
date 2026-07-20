//? neoforge {
/*package doonv.fuzzyautocomplete.platform.neoforge;

import doonv.fuzzyautocomplete.platform.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class NeoforgePlatform implements Platform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public ModLoader loader() {
        return ModLoader.NEOFORGE;
    }

    @Override
    public String mcVersion() {
        return ModList.get().getModContainerById("minecraft")
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("unknown");
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader/^? if > 1.21.7 {^/.getCurrent()/^?}^/.isProduction();
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
 *///?}
