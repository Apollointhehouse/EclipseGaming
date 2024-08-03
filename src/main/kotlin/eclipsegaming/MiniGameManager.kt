package eclipsegaming

import eclipsegaming.EclipseGaming.LOGGER
import eclipsegaming.events.EventHandler
import eclipsegaming.events.EventManager
import eclipsegaming.events.entity.DamageEvent
import eclipsegaming.minigames.MiniGame
import eclipsegaming.utils.PlayerExtensions.game
import net.minecraft.core.player.gamemode.Gamemode
import net.minecraft.server.entity.player.EntityPlayerMP

object MiniGameManager {
	init {
	    EventManager.subscribe(this)
	}

	fun addPlayer(game: MiniGame, player: EntityPlayerMP) {
		if (!game.addPlayer(player)) {
			LOGGER.error("Failed to add player ${player.username} to minigame!")
			return
		}
		player.game = game
		player.sendMessage("Joined ${game.name} queue")
	}

	fun removePlayer(player: EntityPlayerMP) {
		val game = player.game ?: return

		game.removePlayer(player)
		player.setPos(0.0, 12.0, 0.0)
		player.setGamemode(Gamemode.adventure)
		player.heal(20 - player.health)
	}

	@EventHandler
	fun onDamage(event: DamageEvent) {
		val player = event.entity as? EntityPlayerMP ?: return
		val game = player.game ?: return

		if (event.amount >= player.health) {
			game.onPlayerDie(player)
			event.cancel()
		}
	}
}
