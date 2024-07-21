package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2core.base.effects.EffectUtil;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Optional;

public record EffectProcessor(
		Holder<MobEffect> eff,
		IntVariable duration,
		IntVariable amplifier,
		boolean ambient, boolean visible
) implements EntityProcessor<EffectProcessor> {

	public static final MapCodec<EffectProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(e -> e.eff),
			IntVariable.codec("duration", e -> e.duration),
			IntVariable.optionalCodec("amplifier", e -> e.amplifier),
			Codec.BOOL.optionalFieldOf("ambient").forGetter(e -> Optional.of(e.ambient)),
			Codec.BOOL.optionalFieldOf("visible").forGetter(e -> Optional.of(e.visible))
	).apply(i, (a, b, c, d, e) -> new EffectProcessor(a, b,
			c.orElse(IntVariable.of("0")), d.orElse(false), e.orElse(true))));

	@Override
	public ProcessorType<EffectProcessor> type() {
		return EngineRegistry.EFFECT.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		int dur = duration.eval(ctx);
		int amp = amplifier.eval(ctx);
		for (var e : le) {
			EffectUtil.addEffect(e, new MobEffectInstance(eff, dur, amp, ambient, visible, true),
					ctx.user().user());
		}
	}

}
