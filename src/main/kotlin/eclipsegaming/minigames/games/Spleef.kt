package eclipsegaming.minigames.games

import eclipsegaming.minigames.MiniGame
import net.minecraft.core.item.Item
import net.minecraft.core.player.gamemode.Gamemode
import net.minecraft.core.sound.SoundCategory
import net.minecraft.server.entity.player.EntityPlayerMP

class Spleef : MiniGame(name = "Spleef") {

	override fun onPlayerJoin(player: EntityPlayerMP) {
		player.playerNetServerHandler.teleport(0.0, 50.0, 0.0)
		player.inventory.insertItem(Item.toolShovelDiamond.defaultStack, true)
	}

	override fun onPlayerDie(player: EntityPlayerMP) {
		deathSound(player)
		player.sendMessage("You died!")
		player.setGamemode(Gamemode.spectator)
		player.playerNetServerHandler.teleport(0.0, 100.0, 0.0)
	}

	private fun deathSound(player: EntityPlayerMP) {
		world.playSoundEffect(
			player,
			SoundCategory.WEATHER_SOUNDS,
			player.x, player.y, player.z,
			"ambient.weather.thunder",
			10000.0F,
			0.9F
		)
	}
}

