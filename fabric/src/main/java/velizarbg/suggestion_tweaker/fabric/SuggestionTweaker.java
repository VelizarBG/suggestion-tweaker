package velizarbg.suggestion_tweaker.fabric;

import net.fabricmc.api.ModInitializer;
import velizarbg.suggestion_tweaker.Constants;
import velizarbg.suggestion_tweaker.ModConfig;

public class SuggestionTweaker implements ModInitializer {
	@Override
	public void onInitialize() {
		Constants.config = ModConfig.init();
	}
}
