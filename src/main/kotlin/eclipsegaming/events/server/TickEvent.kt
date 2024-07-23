package eclipsegaming.events.server

import eclipsegaming.events.Event
import net.minecraft.server.MinecraftServer

sealed class TickEvent(val server: MinecraftServer) : Event() {
	class Pre(server: MinecraftServer) : TickEvent(server)
	class Post(server: MinecraftServer) : TickEvent(server)
}
