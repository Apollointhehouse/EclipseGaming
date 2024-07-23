package eclipsegaming.minigames

import eclipsegaming.EclipseGaming.LOGGER
import net.minecraft.core.player.gamemode.Gamemode
import net.minecraft.core.world.Dimension
import net.minecraft.core.world.type.WorldTypes
import net.minecraft.server.entity.player.EntityPlayerMP

abstract class MiniGame(val name: String, val minPlayers: Int) {
	val dimension: Dimension = Dimension(name, Dimension.overworld, 1.0f, -1)
		.setDefaultWorldType(WorldTypes.EMPTY)
		.apply { id = prevID++ }
		.also { LOGGER.info("Created dimension: ${it.id}") }

	companion object {
		private var prevID = 3
	}

	open fun tick() {

	}

	open fun onGameStarted(miniGameInstance: MiniGameInstance) {
		miniGameInstance.world.worldTime = 6000 // noon
	}

	open fun onGameEnded() {

	}

	open fun onPlayerAdded(player: EntityPlayerMP) {
		player.sendMessage("Joined $name")
	}

	open fun onPlayerRemoved(player: EntityPlayerMP) {
		player.sendMessage("Left $name")
	}

	open fun getGamemode(gameInstance: MiniGameInstance): Gamemode {
		return Gamemode.adventure;
	}

}
