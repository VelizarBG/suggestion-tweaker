package velizarbg.suggestion_tweaker.forge.mixins;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

import static net.minecraft.command.CommandSource.forEachMatching;
import static net.minecraft.command.CommandSource.shouldSuggest;
import static velizarbg.suggestion_tweaker.Constants.config;

/**
 * Can't mix into static interface methods on forge (SpongePowered/Mixin/issues/497)
 * <p>Yet another Fabric win (FabricMC/Mixin/pull/51)
 */
@Mixin(CommandSource.class)
public interface CommandSourceMixin {
	@Overwrite
	static CompletableFuture<Suggestions> suggestIdentifiers(Iterable<Identifier> candidates, SuggestionsBuilder builder, String prefix) {
		String string = config.isCaseSensitive
			? builder.getRemaining()
			: builder.getRemaining().toLowerCase(Locale.ROOT);

		forEachMatching(candidates, string, prefix, id -> id, id -> builder.suggest(prefix + id));
		return builder.buildFuture();
	}

	@Overwrite
	static CompletableFuture<Suggestions> suggestIdentifiers(Iterable<Identifier> candidates, SuggestionsBuilder builder) {
		String string = config.isCaseSensitive
			? builder.getRemaining()
			: builder.getRemaining().toLowerCase(Locale.ROOT);

		forEachMatching(candidates, string, id -> id, id -> builder.suggest(id.toString()));
		return builder.buildFuture();
	}

	@Overwrite
	static <T> CompletableFuture<Suggestions> suggestFromIdentifier(
		Iterable<T> candidates, SuggestionsBuilder builder, Function<T, Identifier> identifier, Function<T, Message> tooltip
	) {
		String string = config.isCaseSensitive
			? builder.getRemaining()
			: builder.getRemaining().toLowerCase(Locale.ROOT);

		forEachMatching(candidates, string, identifier, object -> builder.suggest((identifier.apply(object)).toString(), tooltip.apply(object)));
		return builder.buildFuture();
	}

	@Overwrite
	static CompletableFuture<Suggestions> suggestMatching(Iterable<String> candidates, SuggestionsBuilder builder) {
		boolean isCaseSensitive = config.isCaseSensitive;
		String string = isCaseSensitive
			? builder.getRemaining()
			: builder.getRemaining().toLowerCase(Locale.ROOT);

		for(String string2 : candidates) {
			if (shouldSuggest(string, isCaseSensitive ? string2 : string2.toLowerCase(Locale.ROOT))) {
				builder.suggest(string2);
			}
		}

		return builder.buildFuture();
	}

	@Overwrite
	static CompletableFuture<Suggestions> suggestMatching(Stream<String> candidates, SuggestionsBuilder builder) {
		boolean isCaseSensitive = config.isCaseSensitive;
		String string = isCaseSensitive
			? builder.getRemaining()
			: builder.getRemaining().toLowerCase(Locale.ROOT);

		candidates.filter(candidate -> shouldSuggest(string, isCaseSensitive ? candidate : candidate.toLowerCase(Locale.ROOT))).forEach(builder::suggest);
		return builder.buildFuture();
	}

	@Overwrite
	static CompletableFuture<Suggestions> suggestMatching(String[] candidates, SuggestionsBuilder builder) {
		boolean isCaseSensitive = config.isCaseSensitive;
		String string = isCaseSensitive
			? builder.getRemaining()
			: builder.getRemaining().toLowerCase(Locale.ROOT);

		for(String string2 : candidates) {
			if (shouldSuggest(string, isCaseSensitive ? string2 : string2.toLowerCase(Locale.ROOT))) {
				builder.suggest(string2);
			}
		}

		return builder.buildFuture();
	}

	@Overwrite
	static <T> CompletableFuture<Suggestions> suggestMatching(
		Iterable<T> candidates, SuggestionsBuilder builder, Function<T, String> suggestionText, Function<T, Message> tooltip
	) {
		boolean isCaseSensitive = config.isCaseSensitive;
		String string = isCaseSensitive
			? builder.getRemaining()
			: builder.getRemaining().toLowerCase(Locale.ROOT);

		for(T object : candidates) {
			String string2 = suggestionText.apply(object);
			if (shouldSuggest(string, isCaseSensitive ? string2 : string2.toLowerCase(Locale.ROOT))) {
				builder.suggest(string2, tooltip.apply(object));
			}
		}

		return builder.buildFuture();
	}
}
