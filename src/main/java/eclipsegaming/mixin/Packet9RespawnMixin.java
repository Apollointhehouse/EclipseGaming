package eclipsegaming.mixin;

import net.minecraft.core.net.packet.Packet9Respawn;
import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Packet9Respawn.class, remap = false)
public class Packet9RespawnMixin {

	@ModifyVariable(method = "<init>(BB)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static byte modifyDimensionId(byte dimensionId) {
		if (dimensionId > Dimension.paradise.id) {
			return (byte) Dimension.overworld.id; // If it's a minigame dimension lie to the client its overworld, so it doesn't crash.
		}

		return dimensionId;
	}
}
