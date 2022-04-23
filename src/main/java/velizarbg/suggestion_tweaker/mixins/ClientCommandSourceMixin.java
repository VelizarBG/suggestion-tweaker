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

import static velizarbg.suggestion_tweaker.SuggestionTweakerClient.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A workaround to the sorting of server-specific suggestions being outsourced to the server.
 */
@Mixin(ClientCommandSource.class)
public class ClientCommandSourceMixin {
	private String currentInput;

	/**
	 * In order to fetch all possible suggestions the last argument needs to be empty.
	 */
	@Redirect(method = "getCompletions", at = @At(target = "Lcom/mojang/brigadier/context/CommandContext;getInput()Ljava/lang/String;", value = "INVOKE"))
	private String processInput(CommandContext<?> context) {
		String input = context.getInput();
		int lastSpaceIndex = input.lastIndexOf(' ');
		currentInput = input.substring(lastSpaceIndex + 1);
		return input.substring(0, lastSpaceIndex + 1);
	}

	@ModifyVariable(method = "onCommandSuggestions", at = @At(target = "Ljava/util/concurrent/CompletableFuture;complete(Ljava/lang/Object;)Z", value = "INVOKE", shift = At.Shift.BEFORE), index = 2, argsOnly = true)
	private Suggestions filterSuggestions(Suggestions suggestions) {
		int start = Integer.MAX_VALUE;
		int end = Integer.MIN_VALUE;
		List<Suggestion> suggestionList = new ArrayList<>();
		for (Suggestion suggestion : suggestions.getList()) {
			if (!config.isCaseSensitive
				? CommandSource.shouldSuggest(currentInput.toLowerCase(Locale.ROOT), suggestion.getText().toLowerCase(Locale.ROOT))
				: CommandSource.shouldSuggest(currentInput, suggestion.getText())
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
