package velizarbg.suggestion_tweaker.mixins;

import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static velizarbg.suggestion_tweaker.Constants.config;

import java.util.Locale;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin {
	@Redirect(method = "sortSuggestions", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestion;getText()Ljava/lang/String;"))
	private String tryLowerCaseSuggestion(Suggestion suggestion) {
		return
			config.isCaseSensitive
				? suggestion.getText()
				: suggestion.getText().toLowerCase(Locale.ROOT);
	}
}
