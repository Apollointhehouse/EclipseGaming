package eclipsegaming.commands

import eclipsegaming.EclipseGaming
import eclipsegaming.MiniGameManager
import net.minecraft.core.net.command.Command
import net.minecraft.core.net.command.CommandHandler
import net.minecraft.core.net.command.CommandSender
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

		val player = sender.player as? EntityPlayerMP ?: return true

		MiniGameManager.removePlayer(player)
		MiniGameManager.addPlayer(game, player)

		return true
	}

	override fun opRequired(strings: Array<String>) = false

	override fun sendCommandSyntax(handler: CommandHandler, sender: CommandSender) {
		sender.sendMessage("/game <name>")
	}


}
