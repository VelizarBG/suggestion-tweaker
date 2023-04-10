package velizarbg.suggestion_tweaker.fabric.mixins;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static velizarbg.suggestion_tweaker.Constants.config;

/**
 * Mixing into libraries - yet another Fabric win (FabricMC/fabric-loader/pull/630)
 */
@Mixin(LiteralCommandNode.class)
public class LiteralCommandNodeMixin {
	@Shadow @Final private String literal;

	@Redirect(method = "listSuggestions", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z"), remap = false)
	private boolean doShouldSuggestCheck(String literalLowerCase, String remainingLowerCase, CommandContext<?> context, SuggestionsBuilder builder) {
		return config.isCaseSensitive
			? CommandSource.shouldSuggest(builder.getRemaining(), literal)
			: CommandSource.shouldSuggest(remainingLowerCase, literalLowerCase);
	}
}
