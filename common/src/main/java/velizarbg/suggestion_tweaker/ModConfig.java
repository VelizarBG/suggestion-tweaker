package velizarbg.suggestion_tweaker;

import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = "suggestion-tweaker")
@Config.Gui.Background("minecraft:textures/block/ice.png")
public class ModConfig implements ConfigData {
	public static ModConfig init() {
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}

	@ConfigEntry.Gui.Tooltip
	public boolean isCaseSensitive = false;

	@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
	@ConfigEntry.Gui.Tooltip
	public FilteringMode filteringMode = FilteringMode.LOOSE;

	public boolean shouldHideSuggestionsWithPrefix = false;

	public String hidePrefix = "_";

	public enum FilteringMode {
		STRICT {
			@Override
			public boolean test(String input, String candidate) {
				return candidate.startsWith(input);
			}
		},
		SLIGHTLY_LOOSE {
			@Override
			public boolean test(String input, String candidate) {
				return candidate.contains(input);
			}
		},
		LOOSE {
			@Override
			public boolean test(String input, String candidate) {
				String[] allWords = input.split("_");
				for (String word : allWords)
					if (!candidate.contains(word))
						return false;

				return true;
			}
		},
		VERY_LOOSE {
			@Override
			public boolean test(String input, String candidate) {
				Char2IntOpenHashMap inputCharCountMap = new Char2IntOpenHashMap();
				for (char c : input.toCharArray())
					inputCharCountMap.addTo(c, 1);

				for (char c : candidate.toCharArray())
					if (inputCharCountMap.containsKey(c)) {
						inputCharCountMap.addTo(c, -1);
						if (inputCharCountMap.get(c) == 0)
							inputCharCountMap.remove(c);
					}

				return inputCharCountMap.isEmpty();
			}
		};

		public abstract boolean test(String input, String candidate);

		@Override
		public String toString() {
			return "suggestion-tweaker.filtering." + this.name().toLowerCase();
		}
	}
}
