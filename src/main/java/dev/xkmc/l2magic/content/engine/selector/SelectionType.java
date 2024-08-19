package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import dev.xkmc.fastprojectileapi.collision.EntityStorageCache;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.function.BiPredicate;

public enum SelectionType {
	NONE((entity, user) -> false),
	ENEMY((entity, user) -> entity instanceof LivingEntity le && le.isAlive() &&
			!user.isPassengerOfSameVehicle(le) && !likesEachOther(user, le, false)),
	ENEMY_NO_FAMILY((entity, user) -> entity instanceof LivingEntity le && le.isAlive() &&
			!user.isPassengerOfSameVehicle(le) && !likesEachOther(user, le, true)),
	ALLY((entity, user) -> entity instanceof LivingEntity le && le.isAlive() && likesEachOther(user, le, false)),
	ALLY_AND_FAMILY((entity, user) -> entity instanceof Player pl && pl.isAlive() ||
			entity instanceof LivingEntity le && le.isAlive() && likesEachOther(user, le, true)),
	ALL((entity, user) -> entity instanceof LivingEntity le && le.isAlive());

	public static final Codec<SelectionType> CODEC = EngineHelper.enumCodec(SelectionType.class, values());

	private static boolean likesEachOther(LivingEntity a, LivingEntity b, boolean checksFamily) {
		if (a.isAlliedTo(b) || b.isAlliedTo(a)) return true;
		if (!checksFamily) return false;
		if (hatesEachOther(a, b)) return false;
		return a.getType() == b.getType();
	}

	private static boolean hatesEachOther(LivingEntity a, LivingEntity b) {
		if (a.getLastHurtMob() == b || a.getLastHurtByMob() == b) return true;
		if (b.getLastHurtMob() == a || b.getLastHurtByMob() == a) return true;
		if (a instanceof Mob ma && ma.getTarget() == b) return true;
		return b instanceof Mob mb && mb.getTarget() == a;
	}

	private final BiPredicate<Entity, LivingEntity> check;

	SelectionType(BiPredicate<Entity, LivingEntity> check) {
		this.check = check;
	}

	public boolean test(Entity target, LivingEntity user) {
		return check.test(target, user);
	}

	public Iterable<Entity> select(Level level, EngineContext ctx, AABB aabb) {
		return EntityStorageCache.get(level).foreach(aabb, x -> check.test(x, ctx.user().user()));
	}
}
