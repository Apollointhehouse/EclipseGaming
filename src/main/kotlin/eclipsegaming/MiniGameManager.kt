package eclipsegaming

import eclipsegaming.minigames.MiniGame
import net.minecraft.server.entity.player.EntityPlayerMP

object MiniGameManager {
	private val games = mutableListOf<MiniGame>()
	private val playerGame = mutableMapOf<EntityPlayerMP, MiniGame>()

	private val MiniGame.isRunning: Boolean get () = games.contains(this)
	private var EntityPlayerMP.game: MiniGame?
		get() = playerGame[this]
		set(value) {
			if (value == null) playerGame.remove(this)
			else playerGame[this] = value
		}

	private fun startMinigame(game: MiniGame) {
		if (game.isRunning) return

		EclipseGaming.LOGGER.info("Starting game: ${game.name}")
		games.add(game)
		game.startGame()
	}

	private fun stopMinigame(game: MiniGame) {
		if (!game.isRunning) return

		EclipseGaming.LOGGER.info("Stopping game: ${game.name}")
		game.endGame()
		games.remove(game)
	}

	fun addPlayer(game: MiniGame, player: EntityPlayerMP) {
		if (player.game != null) {
			EclipseGaming.LOGGER.error("Player ${player.username} is already in minigame!")
			return
		}

		if (!game.isRunning) {
			startMinigame(game)
		}

		player.game = game
		player.game?.addPlayer(player)
	}

	fun removePlayer(player: EntityPlayerMP) {
		val game = player.game ?: return

		game.removePlayer(player)
		player.game = null
		player.setPos(0.0, 150.0, 0.0)

		if (game.playerCount == 0) {
			stopMinigame(game)
		}
	}
}
