package velizarbg.suggestion_tweaker.mixins;

import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

import static velizarbg.suggestion_tweaker.Constants.config;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {
	@Inject(method = "forEachMatching(Ljava/lang/Iterable;Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
	private static <T> void forEachMatching(Iterable<T> candidates, String remaining, Function<T, Identifier> getIdentifier, Consumer<T> action, CallbackInfo ci) {
		for (T candidate : candidates) {
			Identifier identifier = getIdentifier.apply(candidate);

			if (CommandSource.shouldSuggest(remaining, identifier.toString()))
				action.accept(candidate);
		}
		ci.cancel();
	}

	/**
	 * @author VelizarBG
	 * @reason Too niche to not overwrite
	 */
	@Overwrite
	static boolean shouldSuggest(String remaining, String candidate) {
		// if present, split namespace and path to allow searching inside a specific namespace
		String[] remainingId = remaining.split(":", 2);
		if (remainingId.length == 2)
			if (candidate.startsWith(remainingId[0]))
				remaining = remainingId[1];
			else
				return false;

		String[] candidateId = candidate.split(":", 2);
		if (candidateId.length == 2)
			candidate = candidateId[1];

		if (config.shouldHideSuggestionsWithPrefix)
			for (String substring : candidate.split("/"))
				if (substring.startsWith(config.hidePrefix))
					return false;

		return config.filteringMode.test(remaining, candidate);
	}

	@Redirect(
		method = {
			"suggestIdentifiers(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;Ljava/lang/String;)Ljava/util/concurrent/CompletableFuture;",
			"suggestIdentifiers(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;",
			"suggestFromIdentifier(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;",
			"suggestMatching(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;",
			"suggestMatching(Ljava/util/stream/Stream;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;",
			"method_9272(Ljava/lang/String;Ljava/lang/String;)Z",
			"suggestMatching([Ljava/lang/String;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;",
			"suggestMatching(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;Ljava/util/function/Function;Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;"
		},
		at = @At(value = "INVOKE", target = "Ljava/lang/String;toLowerCase(Ljava/util/Locale;)Ljava/lang/String;"))
	private static String handleCaseSensitivity(String string, Locale locale) {
		return config.isCaseSensitive ? string : string.toLowerCase(locale);
	}
}
