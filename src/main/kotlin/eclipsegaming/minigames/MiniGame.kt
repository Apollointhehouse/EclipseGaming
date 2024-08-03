package eclipsegaming.minigames

import eclipsegaming.Config
import eclipsegaming.Config.getGameWorlds
import eclipsegaming.EclipseGaming.LOGGER
import eclipsegaming.MiniGameManager
import eclipsegaming.events.EventManager
import eclipsegaming.utils.PlayerExtensions.game
import net.minecraft.core.player.gamemode.Gamemode
import net.minecraft.core.world.Dimension
import net.minecraft.core.world.type.WorldTypes
import net.minecraft.server.MinecraftServer
import net.minecraft.server.entity.player.EntityPlayerMP
import net.minecraft.server.world.WorldServer
import java.io.File

abstract class MiniGame(
	val name: String,
	private val minPlayers: Int = 1
) {
	protected val mc: MinecraftServer get() = MinecraftServer.getInstance()
	protected val world: WorldServer get() = mc.dimensionWorlds[dimension.id]
	private val players = mutableSetOf<EntityPlayerMP>()
	private val queue = mutableSetOf<EntityPlayerMP>()
	private var running = false

	val dimension: Dimension = Dimension(name, Dimension.overworld, 1.0f, -1)
		.setDefaultWorldType(WorldTypes.EMPTY)
		.apply { id = prevID++ }
		.also { LOGGER.info("Created dimension: ${it.id}") }

	companion object {
		private var prevID = 3
	}

	open fun onGameStarted() {
		world.worldTime = 6000 // noon
	}

	open fun onGameEnded() {

	}

	open fun onPlayerJoin(player: EntityPlayerMP) {
		player.sendMessage("Joined $name")
	}

	open fun onPlayerLeave(player: EntityPlayerMP) {
		player.sendMessage("Left $name")
	}

	open fun onPlayerDie(player: EntityPlayerMP) {
		player.sendMessage("You died!")
		MiniGameManager.removePlayer(player)
	}

	open val gamemode: Gamemode = Gamemode.adventure

	private fun pickMap() {
		val map = Config.cfg.getGameWorlds(this).randomOrNull() ?: return
		val region = File(mc.minecraftDir, "maps/$map/region")
		val dimFolder = File(mc.minecraftDir, "EclipseGaming/dimensions/${dimension.id}/region")
		region.copyTo(dimFolder, overwrite = true)
//		FileUtils.copyAll(region, dimFolder)

		LOGGER.info("Copying map: $map to dimension: ${dimension.id}")
	}

	private fun startGame() {
		running = true
		LOGGER.info("Starting game: $name")
		pickMap()

		EventManager.subscribe(this)
		onGameStarted()
		emptyQueue()
	}

	private fun endGame() {
		running = false
		LOGGER.info("Stopping game: $name")
		EventManager.unsubscribe(this)
		onGameEnded()

		if (queue.size >= minPlayers) startGame()
	}

	private fun emptyQueue() {
		queue.removeAll {
			players.add(it)
			it.setGamemode(gamemode)
			mc.playerList.sendPlayerToOtherDimension(it, dimension.id, false)

			onPlayerJoin(it)
			true
		}
	}

	fun addPlayer(player: EntityPlayerMP): Boolean {
		if (!queue.add(player)) return false
		if (queue.size < minPlayers) return true
		if (!running) startGame()

		return true
	}

	fun removePlayer(player: EntityPlayerMP) {
		if (queue.remove(player)) return
		players.remove(player)

		player.inventory.mainInventory.fill(null)
		player.inventory.armorInventory.fill(null)
		mc.playerList.sendPlayerToOtherDimension(player, 0, false)
		player.game = null

		onPlayerLeave(player)

		if (playerCount == 0) endGame()
	}

	val playerCount: Int get() = players.size
}
