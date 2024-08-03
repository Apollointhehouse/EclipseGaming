package eclipsegaming

import eclipsegaming.EclipseGaming.games
import eclipsegaming.minigames.MiniGame
import turniplabs.halplibe.util.ConfigUpdater
import turniplabs.halplibe.util.TomlConfigHandler
import turniplabs.halplibe.util.toml.Toml

object Config {
	private val updater = ConfigUpdater.fromProperties()
	private val properties = Toml("EclipseGaming Config")
	val cfg: TomlConfigHandler by lazy { TomlConfigHandler(updater, EclipseGaming.MOD_ID, properties) }

	init {
		for (game in games) {
			properties.addCategory(game.name) {
				addCategory("Settings") {
					addEntry("Worlds", "${game.name}1")
				}
			}
		}
	}

	private fun Toml.addCategory(name: String, block: Toml.() -> Unit): Toml = this.addCategory(name).apply(block)
	fun TomlConfigHandler.getGameWorlds(game: MiniGame): List<String> = this.getString("${game.name}.Settings.Worlds").split(",")
}
