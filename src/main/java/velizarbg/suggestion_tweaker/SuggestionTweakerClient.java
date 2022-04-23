package velizarbg.suggestion_tweaker;

import net.fabricmc.api.ClientModInitializer;

public class SuggestionTweakerClient implements ClientModInitializer {
	public static ModConfig config;
	@Override
	public void onInitializeClient() {
		config = ModConfig.init();
	}
}
