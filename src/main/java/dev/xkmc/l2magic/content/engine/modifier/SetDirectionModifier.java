package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

public record SetDirectionModifier(DoubleVariable x, DoubleVariable y, DoubleVariable z)
		implements Modifier<SetDirectionModifier> {

	public static final MapCodec<SetDirectionModifier> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.optionalCodec("x", SetDirectionModifier::x),
			DoubleVariable.optionalCodec("y", SetDirectionModifier::y),
			DoubleVariable.optionalCodec("z", SetDirectionModifier::z)
	).apply(i, (x, y, z) -> new SetDirectionModifier(
			x.orElse(DoubleVariable.ZERO),
			y.orElse(DoubleVariable.ZERO),
			z.orElse(DoubleVariable.ZERO))));

	public static final SetDirectionModifier UP = of("0", "1", "0");

	public static SetDirectionModifier of(String x, String y, String z) {
		return new SetDirectionModifier(
				DoubleVariable.of(x),
				DoubleVariable.of(y),
				DoubleVariable.of(z)
		);
	}

	@Override
	public ModifierType<SetDirectionModifier> type() {
		return EngineRegistry.DIRECTION.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().setDir(new Vec3(
				x.eval(ctx), y.eval(ctx), z.eval(ctx)
		).normalize());
	}

}
