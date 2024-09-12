package dev.xkmc.l2magic.content.entity.core;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

public record ProjectileExpirePacket(
		int id
) implements SerialPacketBase<ProjectileExpirePacket> {

	@Override
	public void handle(Player player) {
		var level = player.level();
		var e = level.getEntity(id);
		if (e instanceof LMProjectile self) {
			self.onClientExpire();
		}
	}

}
