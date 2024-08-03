package eclipsegaming.events.network

import eclipsegaming.events.Event
import net.minecraft.core.net.packet.Packet
import net.minecraft.server.entity.player.EntityPlayerMP

sealed class PacketEvent(val packet: Packet) : Event() {
	class Send(val player: EntityPlayerMP, packet: Packet) : PacketEvent(packet)
	class Receive(packet: Packet) : PacketEvent(packet)
}
