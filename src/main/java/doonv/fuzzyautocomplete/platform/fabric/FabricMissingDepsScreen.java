//? fabric {
package doonv.fuzzyautocomplete.platform.fabric;

import doonv.fuzzyautocomplete.FuzzyAutocomplete;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
//? if >=1.21.11
import org.jspecify.annotations.NonNull;

import java.net.URI;

public class FabricMissingDepsScreen extends Screen {
	public static final boolean YACL_LOADED = FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");

	public FabricMissingDepsScreen() {
		super(translation("screenTitle"));
	}

	@Override
	protected void init() {
		assert !YACL_LOADED;

		int centerX = this.width / 2 - (120 / 2);
		int y = this.height / 2 + 15;

		this.addRenderableWidget(Button.builder(Component.literal("Yet Another Config Lib"), (btn) -> {
			ConfirmLinkScreen.confirmLinkNow(
					//? if >=1.20.3 {
					this, URI.create("https://modrinth.com/mod/yacl")
					//? } else {
					/*"https://modrinth.com/mod/yacl", this, true
					*///? }
			);
		}).bounds(centerX, y, 120, 20).build());

		this.addRenderableWidget(Button.builder(translation("quit"), (btn) -> {
			Minecraft.getInstance().stop();
		}).bounds(this.width / 2 - (100 / 2), this.height - 50, 100, 20).build());
	}

	//~ if >=26.1 'render' -> 'extractRenderState', 'drawCenteredString' -> 'centeredText' {
	@Override
			//~ if >=1.21.11 '/*@NonNull*/' -> '@NonNull'
	public void extractRenderState(/*@NonNull*/ GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);

		int x = this.width / 2;
		int y = this.height / 2;
		graphics.centeredText(this.font, translation("title").withStyle(ChatFormatting.BOLD), this.width / 2, y - 60, 0xFFFFFFFF);
		graphics.centeredText(this.font, translation("description"), x, y - 30, 0xFFFFFFFF);
		graphics.centeredText(this.font, translation("hint"), x, y - 10, 0xAAFFFFFF);
	}
	//~ }

	private static MutableComponent translation(String key) {
		return Component.translatable(FuzzyAutocomplete.MOD_ID + ".missingDeps." + key);
	}
}
//? }
