package eclipsegaming

import eclipsegaming.minigames.MiniGame
import eclipsegaming.minigames.MiniGameInstance
import net.minecraft.server.entity.player.EntityPlayerMP

object MiniGameManager {
	private val miniGames = mutableMapOf<MiniGame, MiniGameInstance>()
	private val playerMiniGameMap = mutableMapOf<EntityPlayerMP, MiniGameInstance>()

	private fun getMinigameWithPlayer(player: EntityPlayerMP): MiniGameInstance? = playerMiniGameMap[player]
	private fun isMinigameRunning(miniGame: MiniGame): Boolean = miniGames.containsKey(miniGame)

	private fun startMinigame(miniGame: MiniGame) {
		if (isMinigameRunning(miniGame)) return

		EclipseGaming.LOGGER.info("Starting game: ${miniGame.name}")
		miniGames[miniGame] = MiniGameInstance(miniGame)
		miniGames[miniGame]?.startGame()
	}

	private fun stopMinigame(miniGame: MiniGame) {
		if (!isMinigameRunning(miniGame)) return

		EclipseGaming.LOGGER.info("Stopping game: ${miniGame.name}")
		miniGames[miniGame]?.endGame()
		miniGames.remove(miniGame)
	}

	fun addPlayer(miniGame: MiniGame, player: EntityPlayerMP) {
		if (getMinigameWithPlayer(player) != null) {
			EclipseGaming.LOGGER.error("Player ${player.username} is already in minigame!")
			return
		}

		if (!isMinigameRunning(miniGame)) {
			startMinigame(miniGame)
		}

		playerMiniGameMap[player] = miniGames[miniGame]!!
		playerMiniGameMap[player]?.addPlayer(player)
	}

	fun removePlayer(player: EntityPlayerMP) {
		val miniGameInstance = getMinigameWithPlayer(player) ?: return

		miniGameInstance.removePlayer(player)
		playerMiniGameMap.remove(player)
		player.setPos(0.0, 150.0, 0.0)

		if (miniGameInstance.getPlayerCount() == 0) {
			stopMinigame(miniGameInstance.game)
		}
	}

	fun tick() {
		miniGames.forEach { (_, miniGameInstance) ->
			miniGameInstance.tick()
		}
	}

}
