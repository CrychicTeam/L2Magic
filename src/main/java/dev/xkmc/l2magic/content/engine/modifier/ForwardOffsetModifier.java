package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record ForwardOffsetModifier(DoubleVariable distance) implements Modifier<ForwardOffsetModifier> {

	public static MapCodec<ForwardOffsetModifier> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("distance", e -> e.distance)
	).apply(i, ForwardOffsetModifier::new));

	public static ForwardOffsetModifier of(String str) {
		return new ForwardOffsetModifier(DoubleVariable.of(str));
	}

	@Override
	public ModifierType<ForwardOffsetModifier> type() {
		return EngineRegistry.FORWARD.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().add(ctx.loc().dir().scale(distance.eval(ctx)));
	}

}
