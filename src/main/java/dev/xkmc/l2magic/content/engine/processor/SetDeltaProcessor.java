package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public record SetDeltaProcessor(
		DoubleVariable x,
		DoubleVariable y,
		DoubleVariable z
) implements EntityProcessor<SetDeltaProcessor> {

	public static final SetDeltaProcessor ZERO = new SetDeltaProcessor(
			DoubleVariable.ZERO, DoubleVariable.ZERO, DoubleVariable.ZERO
	);

	public static final MapCodec<SetDeltaProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("x", e -> e.x),
			DoubleVariable.codec("y", e -> e.y),
			DoubleVariable.codec("z", e -> e.z)
	).apply(i, SetDeltaProcessor::new));

	@Override
	public ProcessorType<SetDeltaProcessor> type() {
		return EngineRegistry.SET_DELTA.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		for (var e : le) {
			e.setDeltaMovement(x.eval(ctx), y.eval(ctx), z.eval(ctx));
			e.hasImpulse = true;
			if (e instanceof Player player) {
				player.hurtMarked = true;
			}
		}
	}

}
