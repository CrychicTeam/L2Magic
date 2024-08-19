package dev.xkmc.l2magic.content.engine.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityFilter;
import dev.xkmc.l2magic.content.engine.core.FilterType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

public record NotFilter(
		EntityFilter<?> child
) implements EntityFilter<NotFilter> {

	public static final MapCodec<NotFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityFilter.CODEC.fieldOf("child").forGetter(e -> e.child)
	).apply(i, NotFilter::new));

	@Override
	public FilterType<NotFilter> type() {
		return EngineRegistry.NOT_FILTER.get();
	}

	@Override
	public boolean test(LivingEntity le, EngineContext ctx) {
		return !child.test(le, ctx);
	}

}
