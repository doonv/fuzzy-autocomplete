package doonv.fuzzyautocomplete.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.ColorField;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import doonv.fuzzyautocomplete.FuzzyAutocomplete;
import net.minecraft.network.chat.Style;
//? if < 1.21 {
/*import net.minecraft.resources.ResourceLocation;
 *///?} else if < 1.21.11 {
/*import net.minecraft.resources.ResourceLocation;
 *///?} else {
import net.minecraft.resources.Identifier;
//?}

import java.awt.*;

public class ClientConfig {
	//? if < 1.21 {
	/*private static final ResourceLocation CONFIG_ID = new ResourceLocation(FuzzyAutocomplete.MOD_ID, "config");
	 *///?} else if < 1.21.11 {
	/*private static final ResourceLocation CONFIG_ID = ResourceLocation.fromNamespaceAndPath(FuzzyAutocomplete.MOD_ID, "config");
	 *///?} else {
	private static final Identifier CONFIG_ID = Identifier.fromNamespaceAndPath(FuzzyAutocomplete.MOD_ID, "config");
	//?}

	public static ConfigClassHandler<ClientConfig> HANDLER = ConfigClassHandler.createBuilder(ClientConfig.class)
			.id(CONFIG_ID)
			.serializer(config -> GsonConfigSerializerBuilder.create(config)
					.setPath(FuzzyAutocomplete.platform().getConfigDir().resolve(
							FuzzyAutocomplete.MOD_ID + ".json5"))
					.appendGsonBuilder(GsonBuilder::setPrettyPrinting)
					.setJson5(true)
					.build())
			.build();

	@SerialEntry
	@AutoGen(category = "settings", group = "matchedStyle")
	@ColorField(allowAlpha = false)
	public Color matchedColor = new Color(0xFFFF00);

	@SerialEntry
	@AutoGen(category = "settings", group = "matchedStyle")
	@TickBox
	public boolean matchedUnderline = true;

	@SerialEntry
	@AutoGen(category = "settings", group = "unmatchedSelectedStyle")
	@ColorField(allowAlpha = false)
	public Color unmatchedSelectedColor = new Color(0xEEEEAA);

	@SerialEntry
	@AutoGen(category = "settings", group = "unmatchedSelectedStyle")
	@TickBox
	public boolean unmatchedSelectedUnderline = false;

	@SerialEntry
	@AutoGen(category = "settings", group = "unmatchedUnselectedStyle")
	@ColorField(allowAlpha = false)
	public Color unmatchedUnselectedColor = new Color(0xAAAAAA);

	@SerialEntry
	@AutoGen(category = "settings", group = "unmatchedUnselectedStyle")
	@TickBox
	public boolean unmatchedUnselectedUnderline = false;

	/// A shortcut for \`HANDLER.instance()\`
	///
	/// @return the config's working instance, used for getting/setting fields.
	public static ClientConfig get() {
		return HANDLER.instance();
	}

	public static Style matchedStyle() {
		return Style.EMPTY.withColor(get().matchedColor.getRGB()).withUnderlined(get().matchedUnderline);
	}

	public static Style unmatchedSelectedStyle() {
		return Style.EMPTY.withColor(get().unmatchedSelectedColor.getRGB()).withUnderlined(
				get().unmatchedSelectedUnderline);
	}

	public static Style unmatchedUnselectedStyle() {
		return Style.EMPTY.withColor(get().unmatchedUnselectedColor.getRGB()).withUnderlined(
				get().unmatchedUnselectedUnderline);
	}
}
