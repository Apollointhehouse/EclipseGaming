package eclipsegaming

import eclipsegaming.commands.GameCommand
import eclipsegaming.commands.LeaveCommand
import eclipsegaming.minigames.MiniGame
import eclipsegaming.minigames.games.Spleef
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turniplabs.halplibe.helper.CommandHelper
import turniplabs.halplibe.util.GameStartEntrypoint

object EclipseGaming: ModInitializer, GameStartEntrypoint {
	const val MOD_ID: String = "eclipsegaming"

	@JvmField val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	@JvmField val games = mutableListOf<MiniGame>()

	init {
		games.add(Spleef())
	}

	override fun onInitialize() {
		LOGGER.info("Eclipse Gaming Initialised!")
		CommandHelper.createCommand(GameCommand())
		CommandHelper.createCommand(LeaveCommand())
	}

	override fun beforeGameStart() {
	}

	override fun afterGameStart() {
	}
}
