package eclipsegaming.commands

import eclipsegaming.EclipseGaming
import eclipsegaming.MiniGameManager
import net.minecraft.core.net.command.Command
import net.minecraft.core.net.command.CommandHandler
import net.minecraft.core.net.command.CommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.server.entity.player.EntityPlayerMP

class GameCommand : Command("game") {
	override fun execute(
		handler: CommandHandler,
		sender: CommandSender,
		strings: Array<String?>
	): Boolean {
		val name = strings.getOrNull(0) ?: return false

		val game = EclipseGaming.games
			.find { game -> game.name.equals(name, ignoreCase = true) } ?: run {
				sender.sendMessage("Game not found")
				return true
			}
		val dimension = game.dimension

		val server = MinecraftServer.getInstance()
		val player = sender.player as? EntityPlayerMP ?: run {
			sender.sendMessage("Cast `sender as EntityPlayerMP` failed")
			return true
		}

		//sender.sendMessage("Teleporting to game: $name")
		MiniGameManager.addPlayer(game, player)
		//server.playerList.sendPlayerToOtherDimension(player, dimension.id)

		return true
	}

	override fun opRequired(strings: Array<String>) = false

	override fun sendCommandSyntax(handler: CommandHandler, sender: CommandSender) {
		sender.sendMessage("/game <name>")
	}


}
