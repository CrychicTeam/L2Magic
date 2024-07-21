package dev.xkmc.l2magic.content.engine.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.core.ClientParticleData;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleOption;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.render.RandomColorParticle;
import dev.xkmc.l2magic.content.particle.render.TransitionParticleSprite;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;

public record TransitionParticleInstance(
		ColorVariable start, ColorVariable end,
		DoubleVariable scale, DoubleVariable speed,
		IntVariable life)
		implements ParticleInstance<TransitionParticleInstance>, RandomColorParticle {

	public static final MapCodec<TransitionParticleInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ColorVariable.CODEC.fieldOf("start").forGetter(e -> e.start),
			ColorVariable.CODEC.fieldOf("end").forGetter(e -> e.end),
			DoubleVariable.codec("scale", TransitionParticleInstance::scale),
			DoubleVariable.codec("speed", ParticleInstance::speed),
			IntVariable.codec("life", e -> e.life)
	).apply(i, TransitionParticleInstance::new));

	@Override
	public EngineType<TransitionParticleInstance> type() {
		return EngineRegistry.TRANSITION_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		int life = life().eval(ctx);
		float scale = (float) scale().eval(ctx) * ClientParticleData.randSize(ctx);
		return new LMGenericParticleOption(new ClientParticleData(life, true, scale,
				ctx, SimpleMotion.DUST, new TransitionParticleSprite(
				RenderTypePreset.NORMAL,
				randomizeColor(ctx.rand(), start.eval(ctx)),
				randomizeColor(ctx.rand(), end.eval(ctx))
		)));
	}

}
