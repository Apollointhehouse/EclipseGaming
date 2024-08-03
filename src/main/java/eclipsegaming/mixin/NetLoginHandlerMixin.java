package eclipsegaming.mixin;

import net.minecraft.core.Global;
import net.minecraft.core.net.packet.Packet254PingHandshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetLoginHandler.class, remap = false)
public class NetLoginHandlerMixin {
	@Shadow private MinecraftServer mcServer;

	@Inject(method = "handlePingHandshake", at = @At("HEAD"), cancellable = true)
	private void handlePingHandshake(Packet254PingHandshake packet, CallbackInfo ci) {
		NetLoginHandler self = (NetLoginHandler) (Object) this;

		if (packet.pingHostString.equals("BTAPingHost")) {
			String msg = "§1\u000029184\u0000"
				+ Global.VERSION
				+ '\u0000'
				+ mcServer.motd
				+ '\u0000'
				+ (int)(mcServer.playerList.playerEntities.size() * 1.5f)
				+ '\u0000'
				+ (int)(mcServer.maxPlayers * 1.5f);

			self.kickUser(msg, true);
			ci.cancel();
		}
	}
}
