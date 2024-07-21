package eclipsegaming.minigames

import eclipsegaming.EclipseGaming.LOGGER
import net.minecraft.core.world.Dimension
import net.minecraft.core.world.type.WorldTypes

abstract class MiniGame(val name: String) {
	val dimension: Dimension = Dimension(name, Dimension.overworld, 1.0f, -1)
		.setDefaultWorldType(WorldTypes.EMPTY)
		.apply { id = prevID++ }
		.also { LOGGER.info("Created dimension: ${it.id}") }

	companion object {
		private var prevID = 3
	}
}
