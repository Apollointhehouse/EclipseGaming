package eclipsegaming.mixin;

import eclipsegaming.events.network.PacketEvent;
import net.minecraft.core.net.NetworkManager;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = NetworkManager.class, remap = false)
public class NetworkManagerMixin {
	@Redirect(method = "processReadPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/net/packet/Packet;processPacket(Lnet/minecraft/core/net/handler/NetHandler;)V"))
	void onProcessPacket(Packet packet, NetHandler netHandler) {
		PacketEvent.Receive event = new PacketEvent.Receive(packet);
		event.call();

		if (!event.getCancelled())
			event.getPacket().processPacket(netHandler);
	}
}
