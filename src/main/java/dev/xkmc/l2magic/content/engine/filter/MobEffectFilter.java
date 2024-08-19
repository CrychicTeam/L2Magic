package dev.xkmc.l2magic.content.engine.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2core.init.L2LibReg;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityFilter;
import dev.xkmc.l2magic.content.engine.core.FilterType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public record MobEffectFilter(
		Holder<MobEffect> eff
) implements EntityFilter<MobEffectFilter> {

	public static final MapCodec<MobEffectFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(e -> e.eff)
	).apply(i, MobEffectFilter::new));

	@Override
	public FilterType<MobEffectFilter> type() {
		return EngineRegistry.EFFECT_FILTER.get();
	}

	@Override
	public boolean test(LivingEntity e, EngineContext ctx) {
		if (e.level().isClientSide())
			return L2LibReg.EFFECT.type().getExisting(e).map(x -> x.map.containsKey(eff)).orElse(false);
		else return e.hasEffect(eff);
	}

}
