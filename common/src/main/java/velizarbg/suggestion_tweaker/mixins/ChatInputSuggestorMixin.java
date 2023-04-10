package velizarbg.suggestion_tweaker.mixins;

import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Locale;

import static velizarbg.suggestion_tweaker.Constants.config;
import static velizarbg.suggestion_tweaker.ModConfig.FilteringMode.*;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin {
	@Shadow private static int getStartOfCurrentWord(String input) {
		throw new AssertionError();
	}
	@Shadow @Final TextFieldWidget textField;

	/**
	 * @author VelizarBG
	 * @reason Too niche to not overwrite
	 */
	@Overwrite
	private List<Suggestion> sortSuggestions(Suggestions suggestions) {
		String command = textField.getText().substring(0, textField.getCursor());
		// To make sorting command literals work
		if (command.startsWith("/"))
			command = command.substring(1);
		int startOfCurrentWord = getStartOfCurrentWord(command);
		String remaining = command.substring(startOfCurrentWord);
		// To make sorting tags work
		if (remaining.startsWith("#"))
			remaining = remaining.substring(1);
		List<Suggestion> strictList = Lists.newArrayList();
		List<Suggestion> slightlyLooseList = Lists.newArrayList();
		List<Suggestion> looseList = Lists.newArrayList();
		List<Suggestion> veryLooseList = Lists.newArrayList();

		if (remaining.contains(":"))
			remaining = remaining.substring(remaining.indexOf(':') + 1);
		boolean isCaseSensitive = config.isCaseSensitive;
		if (!isCaseSensitive)
			remaining = remaining.toLowerCase(Locale.ROOT);

		for(Suggestion suggestion : suggestions.getList()) {
			String suggestionText = suggestion.getText();
			if (suggestionText.contains(":"))
				suggestionText = suggestionText.substring(suggestionText.indexOf(':') + 1);
			if (!isCaseSensitive)
				suggestionText = suggestionText.toLowerCase(Locale.ROOT);

			if (STRICT.test(remaining, suggestionText))
				strictList.add(suggestion);
			else if (SLIGHTLY_LOOSE.test(remaining, suggestionText))
				slightlyLooseList.add(suggestion);
			else if (LOOSE.test(remaining, suggestionText))
				looseList.add(suggestion);
			else
				veryLooseList.add(suggestion);
		}

		strictList.addAll(slightlyLooseList);
		strictList.addAll(looseList);
		strictList.addAll(veryLooseList);
		return strictList;
	}
}
