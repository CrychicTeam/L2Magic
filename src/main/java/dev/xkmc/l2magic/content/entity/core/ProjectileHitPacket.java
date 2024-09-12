package dev.xkmc.l2magic.content.entity.core;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record ProjectileHitPacket(
		int id, int target
) implements SerialPacketBase<ProjectileHitPacket> {

	@Override
	public void handle(Player player) {
		var level = player.level();
		var e = level.getEntity(id);
		var t = level.getEntity(target);
		if (e instanceof LMProjectile self && t instanceof LivingEntity le) {
			self.onClientHitEntity(le);
		}
	}

}
