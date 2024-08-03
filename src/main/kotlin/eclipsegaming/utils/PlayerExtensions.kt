package eclipsegaming.utils

import eclipsegaming.minigames.MiniGame
import net.minecraft.server.entity.player.EntityPlayerMP

object PlayerExtensions {
	private val playerGame = mutableMapOf<EntityPlayerMP, MiniGame>()

	var EntityPlayerMP.game: MiniGame?
		get() = playerGame[this]
		set(value) {
			if (value == null) playerGame.remove(this)
			else playerGame[this] = value
		}
}
