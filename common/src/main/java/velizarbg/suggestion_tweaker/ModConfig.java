package velizarbg.suggestion_tweaker;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "suggestion-tweaker")
@Config.Gui.Background("minecraft:textures/block/ice.png")
public class ModConfig implements ConfigData {
	public static ModConfig init() {
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}

	@Comment("Controls how some suggestions are filtered and sorted based on case")
	public boolean isCaseSensitive = false;
}
