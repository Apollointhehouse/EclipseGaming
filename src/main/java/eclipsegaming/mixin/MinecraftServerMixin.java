package eclipsegaming.mixin;

import eclipsegaming.events.server.TickEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
abstract public class MinecraftServerMixin {
	@Inject(method = "doTick", at = @At("HEAD"))
	public void doPreTick(CallbackInfo ci) {
		MinecraftServer server = (MinecraftServer) (Object) this;
		TickEvent.Pre event = new TickEvent.Pre(server);
		event.call();
	}

	@Inject(method = "doTick", at = @At("HEAD"))
	public void doPostTick(CallbackInfo ci) {
		MinecraftServer server = (MinecraftServer) (Object) this;
		TickEvent.Post event = new TickEvent.Post(server);
		event.call();
	}

//	@Inject(method = "initWorld", at = @At(value = "INVOKE", target = "Lorg/apache/log4j/Logger;info(Ljava/lang/Object;)V", shift = At.Shift.BEFORE))
//	public void fixPreLoadDim(ISaveFormat saveFormat, String worldDirName, long l, CallbackInfo ci, @Local(ordinal = 1) int j) {
//
//	}
}
