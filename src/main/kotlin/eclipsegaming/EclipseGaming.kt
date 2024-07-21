package eclipsegaming

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object EclipseGaming: ModInitializer {
	const val MOD_ID: String = "eclipsegaming"

	@JvmField
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOGGER.info("Eclipse Gaming Initialised!")


	}
}
