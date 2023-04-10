package velizarbg.suggestion_tweaker.mixins;

import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;
import java.util.function.Function;

import static velizarbg.suggestion_tweaker.Constants.config;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {
	/**
	 * @author VelizarBG
	 * @reason Too niche to not overwrite
	 */
	@Overwrite
	static <T> void forEachMatching(Iterable<T> candidates, String remaining, Function<T, Identifier> getIdentifier, Consumer<T> action) {
		for (T candidate : candidates) {
			Identifier identifier = getIdentifier.apply(candidate);

			if (CommandSource.shouldSuggest(remaining, identifier.toString()))
				action.accept(candidate);
		}
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

		return config.filteringMode.test(remaining, candidate);
	}
}
