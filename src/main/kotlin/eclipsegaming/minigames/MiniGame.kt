package eclipsegaming.minigames

import eclipsegaming.EclipseGaming.LOGGER
import eclipsegaming.events.EventManager
import net.minecraft.core.player.gamemode.Gamemode
import net.minecraft.core.world.Dimension
import net.minecraft.core.world.type.WorldTypes
import net.minecraft.server.MinecraftServer
import net.minecraft.server.entity.player.EntityPlayerMP
import net.minecraft.server.world.WorldServer

abstract class MiniGame(val name: String, val minPlayers: Int) {
	private val mc: MinecraftServer get() = MinecraftServer.getInstance()
	val dimension: Dimension = Dimension(name, Dimension.overworld, 1.0f, -1)
		.setDefaultWorldType(WorldTypes.EMPTY)
		.apply { id = prevID++ }
		.also { LOGGER.info("Created dimension: ${it.id}") }

	val world: WorldServer get() = mc.dimensionWorlds[dimension.id]
	val players = mutableSetOf<EntityPlayerMP>()

	companion object {
		private var prevID = 3
	}

	open fun onGameStarted() {
		world.worldTime = 6000 // noon
	}

	open fun onGameEnded() {

	}

	open fun onPlayerAdded(player: EntityPlayerMP) {
		player.sendMessage("Joined $name")
	}

	open fun onPlayerRemoved(player: EntityPlayerMP) {
		player.sendMessage("Left $name")
	}

//	open fun onPlayerDie(player: EntityPlayerMP) {
//		player.sendMessage("You died!")
//		player.setGamemode(Gamemode.spectator)
//	}

	open val gamemode: Gamemode = Gamemode.adventure

	fun startGame() {
		EventManager.subscribe(this)
		onGameStarted()
	}

	fun endGame() {
		EventManager.unsubscribe(this)
		onGameEnded()
	}

	fun addPlayer(player: EntityPlayerMP) {
		players.add(player)

		player.setGamemode(gamemode)
		mc.playerList.sendPlayerToOtherDimension(player, dimension.id)

		onPlayerAdded(player)
	}

	fun removePlayer(player: EntityPlayerMP) {
		players.remove(player)

		player.inventory.mainInventory.fill(null)
		player.inventory.armorInventory.fill(null)
		mc.playerList.sendPlayerToOtherDimension(player, 0)

		onPlayerRemoved(player)
	}

	val playerCount: Int get() = players.size
}
