package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.content.particle.render.TransitionParticleSprite;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record TransitionParticleData(
		RenderTypePreset renderType, ColorVariable start, ColorVariable end
) implements ParticleRenderData<TransitionParticleData> {

	public static final MapCodec<TransitionParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			RenderTypePreset.CODEC.fieldOf("renderType").forGetter(e -> e.renderType),
			ColorVariable.CODEC.fieldOf("start").forGetter(e -> e.start),
			ColorVariable.CODEC.fieldOf("end").forGetter(e -> e.end)
	).apply(i, TransitionParticleData::new));

	@Override
	public ParticleRenderType<TransitionParticleData> type() {
		return EngineRegistry.TRANSITION_RENDER.get();
	}

	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		return new TransitionParticleSprite(renderType, start.eval(ctx), end.eval(ctx));
	}

}
