package dev.xkmc.l2magic.content.engine.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.core.ClientParticleData;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleOption;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.render.BlockSprite;
import dev.xkmc.l2magic.content.particle.render.SpriteGeom;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record BlockParticleInstance(
		Block block, DoubleVariable speed,
		DoubleVariable scale, IntVariable life,
		boolean breaking
) implements ParticleInstance<BlockParticleInstance> {

	public static final MapCodec<BlockParticleInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(e -> e.block),
			DoubleVariable.codec("speed", ParticleInstance::speed),
			DoubleVariable.codec("scale", e -> e.scale),
			IntVariable.codec("life", e -> e.life),
			Codec.BOOL.fieldOf("breaking").forGetter(e -> e.breaking)
	).apply(i, BlockParticleInstance::new));

	@Override
	public EngineType<BlockParticleInstance> type() {
		return EngineRegistry.BLOCK_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new LMGenericParticleOption(new ClientParticleData(
				life.eval(ctx), breaking, (float) scale.eval(ctx) * ClientParticleData.randSize(ctx), ctx,
				breaking() ? SimpleMotion.BREAKING : SimpleMotion.ZERO,
				new BlockSprite(RenderTypePreset.BLOCK, block.defaultBlockState(),
						BlockPos.containing(ctx.loc().pos()),
						breaking ? SpriteGeom.breaking(ctx.rand()) : SpriteGeom.INSTANCE)
		));
	}

}
