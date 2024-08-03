package eclipsegaming.mixin;

import eclipsegaming.events.network.PacketEvent;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetServerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetServerHandler.class, remap = false)
public class NetServerHandlerMixin {
	@Shadow private EntityPlayerMP playerEntity;
	@Shadow private int field_15_f;
	@Shadow private int field_22004_g;

	@Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
	void onSendPacket(Packet packet, CallbackInfo ci) {
		NetServerHandler handler = (NetServerHandler) (Object) this;
		PacketEvent.Send event = new PacketEvent.Send(playerEntity, packet);
		event.call();

		if (!event.getCancelled()) {
			handler.netManager.addToSendQueue(event.getPacket());
			field_22004_g = field_15_f;
		}

		ci.cancel();
	}
}
