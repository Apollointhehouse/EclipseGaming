package eclipsegaming.mixin;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.net.packet.Packet29DestroyEntity;
import net.minecraft.core.net.packet.Packet41EntityPlayerGamemode;
import net.minecraft.core.net.packet.Packet74GameRule;
import net.minecraft.core.net.packet.Packet9Respawn;
import net.minecraft.core.world.Dimension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.PlayerList;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PlayerList.class, remap = false)
public abstract class PlayerListMixin {

	@Shadow
	@Final
	private MinecraftServer server;

	@Shadow
	public abstract void func_28172_a(EntityPlayerMP player);

	@Shadow
	public abstract void func_28170_a(EntityPlayerMP entityplayermp, WorldServer worldserver);

	@Shadow
	public abstract void func_30008_g(EntityPlayerMP entityplayermp);

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void sendPlayerToOtherDimension(EntityPlayerMP player, int targetDim) {
		WorldServer oldDim = this.server.getDimensionWorld(player.dimension);

		player.dimension = targetDim;
		WorldServer newDim = this.server.getDimensionWorld(player.dimension);

		//for (Entity ent : oldDim.loadedEntityList) {
		//	player.playerNetServerHandler.sendPacket(new Packet29DestroyEntity(ent.id));
		//}

		player.playerNetServerHandler.sendPacket(new Packet9Respawn((byte) Dimension.paradise.id, (byte) Registries.WORLD_TYPES.getNumericIdOfItem(newDim.worldType)));
		player.playerNetServerHandler.sendPacket(new Packet9Respawn((byte) Dimension.overworld.id, (byte) Registries.WORLD_TYPES.getNumericIdOfItem(newDim.worldType)));


		oldDim.removePlayer(player);
		player.removed = false;

		double playerPosX = player.x * Dimension.getCoordScale(oldDim.dimension, newDim.dimension);
		double playerPosZ = player.z * Dimension.getCoordScale(oldDim.dimension, newDim.dimension);

		player.moveTo(
			playerPosX,
			player.y,
			playerPosZ,
			player.yRot,
			player.xRot
		);

		if (player.isAlive()) {
			oldDim.updateEntityWithOptionalForce(player, false);
		}

		if (player.isAlive()) {
			newDim.entityJoinedWorld(player);

			player.moveTo(playerPosX, player.y, playerPosZ, player.yRot, player.xRot);

			newDim.updateEntityWithOptionalForce(player, false);

			player.moveTo(playerPosX, player.y, playerPosZ, player.yRot, player.xRot);
		}

		this.func_28172_a(player);

		player.playerNetServerHandler.teleportAndRotate(player.x, player.y, player.z, player.yRot, player.xRot);
		player.playerNetServerHandler.sendPacket(new Packet41EntityPlayerGamemode(player.id, player.getGamemode().getId()));

		player.setWorld(newDim);

		this.func_28170_a(player, newDim);
		this.func_30008_g(player);

		player.playerNetServerHandler.sendPacket(new Packet74GameRule(this.server.getDimensionWorld(Dimension.overworld.id).getLevelData().getGameRules()));
	}
}
