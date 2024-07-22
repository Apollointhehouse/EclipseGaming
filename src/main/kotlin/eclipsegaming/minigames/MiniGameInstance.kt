package eclipsegaming.minigames

import net.minecraft.server.MinecraftServer
import net.minecraft.server.entity.player.EntityPlayerMP

class MiniGameInstance(val game: MiniGame) {
	private val mc = MinecraftServer.getInstance()
	private val players = mutableSetOf<EntityPlayerMP>()
	private var gameState = MiniGameState.WAITING

	fun tick() {
		if (gameState == MiniGameState.ACTIVE) {
			game.tick()
		}
	}

	fun addPlayer(player: EntityPlayerMP) {
		players.add(player)

		mc.playerList.sendPlayerToOtherDimension(player, game.dimension.id)

		game.onPlayerAdded(player)
	}

	fun removePlayer(player: EntityPlayerMP) {
		players.remove(player)

		player.inventory.mainInventory.fill(null)
		player.inventory.armorInventory.fill(null)
		mc.playerList.sendPlayerToOtherDimension(player, 0)

		game.onPlayerRemoved(player)
	}

	fun getPlayerCount(): Int = players.size

}
