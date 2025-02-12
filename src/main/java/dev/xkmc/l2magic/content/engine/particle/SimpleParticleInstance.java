package dev.xkmc.l2magic.content.engine.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;

public record SimpleParticleInstance(ParticleType<?> particle, DoubleVariable speed)
		implements ParticleInstance<SimpleParticleInstance> {

	public static final MapCodec<SimpleParticleInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("particle").forGetter(e -> e.particle),
			DoubleVariable.codec("speed", ParticleInstance::speed)
	).apply(i, SimpleParticleInstance::new));

	@Override
	public EngineType<SimpleParticleInstance> type() {
		return EngineRegistry.SIMPLE_PARTICLE.get();
	}

	@Nullable
	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return particle instanceof ParticleOptions opt ? opt : null;
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		if (!(particle instanceof ParticleOptions)) {
			ctx.of("particle").error("Invalid particle type");
			ParticleInstance.super.verify(ctx);
			return false;
		}
		return ParticleInstance.super.verify(ctx);
	}
}
