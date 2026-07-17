package doonv.fuzzyautocomplete.mixin;

//? fabric {

import net.fabricmc.loader.api.FabricLoader;
//?} neoforge {
/*import net.neoforged.fml.ModList;
 *///?}
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class FuzzyAutocompleteMixinPlugin implements IMixinConfigPlugin {
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.contains("NbtAcCustomSuggestionMixin")) {
			//? fabric {
			return FabricLoader.getInstance().isModLoaded("nbt_ac");
			//?} neoforge {
			/*return false; // NbtAc is Fabric-only
			 *///?}
		}
		return true;
	}

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {return null;}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {return null;}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
