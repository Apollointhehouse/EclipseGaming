package eclipsegaming.mixin;

import eclipsegaming.minigames.MiniGame;
import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static eclipsegaming.EclipseGaming.games;

@Mixin(value = Dimension.class, remap = false)
public class DimensionMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void staticInit(CallbackInfo ci) {
		for (MiniGame game : games) {
			Dimension.registerDimension(game.getDimension().id, game.getDimension());
		}
	}
}
