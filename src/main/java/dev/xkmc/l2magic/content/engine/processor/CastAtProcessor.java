package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public record CastAtProcessor(
		PosType pos,
		DirType dir,
		ConfiguredEngine<?> child
) implements EntityProcessor<CastAtProcessor> {

	public enum PosType {
		ORIGINAL, BOTTOM, CENTER, EYE
	}

	public enum DirType {
		ORIGINAL, TO_SELF, UP, HORIZONTAL, LOOKING
	}

	public static final Codec<PosType> POS_CODEC = EngineHelper.enumCodec(PosType.class, PosType.values());
	public static final Codec<DirType> DIR_CODEC = EngineHelper.enumCodec(DirType.class, DirType.values());

	public static final MapCodec<CastAtProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			POS_CODEC.fieldOf("pos").forGetter(e -> e.pos),
			DIR_CODEC.fieldOf("dir").forGetter(e -> e.dir),
			ConfiguredEngine.codec("child", e -> e.child)
	).apply(i, CastAtProcessor::new));

	@Override
	public ProcessorType<CastAtProcessor> type() {
		return EngineRegistry.CAST_AT.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		for (LivingEntity e : le) {
			Vec3 p = switch (pos) {
				case ORIGINAL -> ctx.loc().pos();
				case BOTTOM -> e.position();
				case CENTER -> new Vec3(e.getX(), e.getY(0.5), e.getZ());
				case EYE -> e.getEyePosition();
			};
			Vec3 d = switch (dir) {
				case ORIGINAL -> ctx.loc().dir();
				case TO_SELF -> p.subtract(ctx.loc().pos()).normalize();
				case UP -> LocationContext.UP;
				case HORIZONTAL -> SpellContext.getForward(e).multiply(1, 0, 1).normalize();
				case LOOKING -> SpellContext.getForward(e);
			};
			ctx.execute(LocationContext.of(p, d), child);
		}
	}
}
