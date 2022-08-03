package velizarbg.suggestion_tweaker;

import net.fabricmc.api.ModInitializer;

public class SuggestionTweaker implements ModInitializer {
	@Override
	public void onInitialize() {
		Constants.config = ModConfig.init();
	}
}
