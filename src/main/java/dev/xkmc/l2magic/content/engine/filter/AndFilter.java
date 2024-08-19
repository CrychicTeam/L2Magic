package dev.xkmc.l2magic.content.engine.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityFilter;
import dev.xkmc.l2magic.content.engine.core.FilterType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public record AndFilter(
		List<EntityFilter<?>> list
) implements EntityFilter<AndFilter> {

	public static final MapCodec<AndFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityFilter.CODEC.listOf().fieldOf("list").forGetter(e -> e.list)
	).apply(i, AndFilter::new));

	@Override
	public FilterType<AndFilter> type() {
		return EngineRegistry.AND_FILTER.get();
	}

	@Override
	public boolean test(LivingEntity le, EngineContext ctx) {
		for (var e : list) {
			if (!e.test(le, ctx)) {
				return false;
			}
		}
		return true;
	}

}
