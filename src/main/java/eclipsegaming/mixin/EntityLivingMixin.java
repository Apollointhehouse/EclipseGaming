package eclipsegaming.mixin;

import eclipsegaming.events.entity.DamageEvent;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityLiving.class, remap = false)
class EntityLivingMixin {
	@Inject(method = "damageEntity", at = @At("HEAD"), cancellable = true)
	private void damageEntity(int i, DamageType damageType, CallbackInfo ci) {
		EntityLiving entity = (EntityLiving) (Object) this;
		DamageEvent event = new DamageEvent(entity, i, damageType);
		event.call();

		if (!event.getCancelled()) {
			entity.setHealthRaw(entity.getHealth() - event.getAmount());
		}
		ci.cancel();
	}
}

