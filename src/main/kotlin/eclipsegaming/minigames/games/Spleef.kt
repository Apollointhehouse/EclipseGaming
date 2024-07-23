package eclipsegaming.minigames.games

import eclipsegaming.minigames.MiniGame
import net.minecraft.core.block.Block
import net.minecraft.core.item.Item
import net.minecraft.server.entity.player.EntityPlayerMP

class Spleef : MiniGame(name = "Spleef", minPlayers = 3) {

	private val platformY = 80

	override fun onGameStarted() {
		super.onGameStarted()

		for (x in -15..15) {
			for (z in -15..15) {
				world.setBlock(x, platformY, z, Block.blockSnow.id)
			}
		}
	}

	override fun onPlayerAdded(player: EntityPlayerMP) {
		player.setPos(0.0, platformY + 1.0, 0.0)
		player.inventory.insertItem(Item.toolShovelDiamond.defaultStack, true)
	}
}
