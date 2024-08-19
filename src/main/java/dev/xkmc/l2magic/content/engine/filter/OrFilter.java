package dev.xkmc.l2magic.content.engine.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityFilter;
import dev.xkmc.l2magic.content.engine.core.FilterType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public record OrFilter(
		List<EntityFilter<?>> list
) implements EntityFilter<OrFilter> {

	public static final MapCodec<OrFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityFilter.CODEC.listOf().fieldOf("list").forGetter(e -> e.list)
	).apply(i, OrFilter::new));

	@Override
	public FilterType<OrFilter> type() {
		return EngineRegistry.OR_FILTER.get();
	}

	@Override
	public boolean test(LivingEntity le, EngineContext ctx) {
		for (var e : list) {
			if (e.test(le, ctx)) {
				return true;
			}
		}
		return false;
	}

}
