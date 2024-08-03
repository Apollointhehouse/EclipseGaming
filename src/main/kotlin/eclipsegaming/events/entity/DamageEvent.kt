package eclipsegaming.events.entity

import eclipsegaming.events.Event
import net.minecraft.core.entity.Entity
import net.minecraft.core.util.helper.DamageType

class DamageEvent(
	val entity: Entity,
	var amount: Int,
	val type: DamageType?
) : Event()
