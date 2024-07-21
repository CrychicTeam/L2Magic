package dev.xkmc.l2magic.content.engine.helper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record EffectInstanceEntry(
		Holder<MobEffect> effect, IntVariable duration, IntVariable amplifier
) {

	public static final Codec<EffectInstanceEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(e -> e.effect),
			IntVariable.codec("duration", e -> e.duration),
			IntVariable.optionalCodec("amplifier", e -> e.amplifier)
	).apply(i, (e, d, a) -> new EffectInstanceEntry(e, d, a.orElse(IntVariable.of("0")))));

	public MobEffectInstance get(EngineContext ctx) {
		int dur = duration().eval(ctx);
		int amp = amplifier().eval(ctx);
		return new MobEffectInstance(effect, dur, amp);
	}

}
