package velizarbg.suggestion_tweaker.mixins;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static velizarbg.suggestion_tweaker.Constants.config;

/**
 * A workaround to the filtering of server-specific suggestions being outsourced to the server.
 */
@Mixin(ClientCommandSource.class)
public class ClientCommandSourceMixin {
	private String currentInput;
	private static final char[] SPECIAL_CUTOFF_CHARS = new char[]{'[', '=', ','};

	/**
	 * In order to fetch all possible suggestions the last argument needs to be empty.
	 */
	@Redirect(method = "getCompletions", at = @At(target = "Lcom/mojang/brigadier/context/CommandContext;getInput()Ljava/lang/String;", value = "INVOKE"))
	private String processInput(CommandContext<?> context) {
		String input = context.getInput();
		int cutoffIndex = input.lastIndexOf(' ');
		// special cutoff chars are added to fix #6
		for (char c : SPECIAL_CUTOFF_CHARS) {
			int index = input.lastIndexOf(c);
			if (index > cutoffIndex)
				cutoffIndex = index;
		}
		currentInput = input.substring(cutoffIndex + 1);
		return input.substring(0, cutoffIndex + 1);
	}

	@ModifyVariable(method = "onCommandSuggestions", at = @At(target = "Ljava/util/concurrent/CompletableFuture;complete(Ljava/lang/Object;)Z", value = "INVOKE", shift = At.Shift.BEFORE), index = 2, argsOnly = true)
	private Suggestions filterSuggestions(Suggestions suggestions) {
		int start = Integer.MAX_VALUE;
		int end = Integer.MIN_VALUE;
		List<Suggestion> suggestionList = new ArrayList<>();
		for (Suggestion suggestion : suggestions.getList()) {
			if (config.isCaseSensitive
				? CommandSource.shouldSuggest(currentInput, suggestion.getText())
				: CommandSource.shouldSuggest(currentInput.toLowerCase(Locale.ROOT), suggestion.getText().toLowerCase(Locale.ROOT))
			) {
				suggestionList.add(new Suggestion(
					new StringRange(
						suggestion.getRange().getStart(),
						suggestion.getRange().getEnd() + currentInput.length()
					),
					suggestion.getText(),
					suggestion.getTooltip()
				));
				start = Math.min(suggestion.getRange().getStart(), start);
				end = Math.max(suggestion.getRange().getEnd(), end);
			}
		}
		return new Suggestions(new StringRange(start, end), suggestionList);
	}
}
