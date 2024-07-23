package eclipsegaming.commands

import eclipsegaming.MiniGameManager
import net.minecraft.core.net.command.Command
import net.minecraft.core.net.command.CommandHandler
import net.minecraft.core.net.command.CommandSender
import net.minecraft.server.entity.player.EntityPlayerMP

class LeaveCommand : Command("leave") {
	override fun execute(
		handler: CommandHandler,
		sender: CommandSender,
		strings: Array<String?>
	): Boolean {
		val player = sender.player as? EntityPlayerMP ?: run {
			sender.sendMessage("Cast `sender as EntityPlayerMP` failed")
			return true
		}

		sender.sendMessage("Leaving...")
		MiniGameManager.removePlayer(player)
		//MinecraftServer.getInstance().playerList.sendPlayerToOtherDimension(player, 0)

		return true
	}

	override fun opRequired(strings: Array<out String>?): Boolean = false

	override fun sendCommandSyntax(handler: CommandHandler, sender: CommandSender) {
		sender.sendMessage("/leave")
	}
}
