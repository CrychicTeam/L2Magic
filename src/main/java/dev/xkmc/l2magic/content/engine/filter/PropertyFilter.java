package dev.xkmc.l2magic.content.engine.filter;

import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityFilter;
import dev.xkmc.l2magic.content.engine.core.FilterType;
import net.minecraft.world.entity.LivingEntity;

public record PropertyFilter(

) implements EntityFilter<PropertyFilter> {

	@Override
	public FilterType<PropertyFilter> type() {
		return null;//TODO
	}

	@Override
	public boolean test(LivingEntity le, EngineContext ctx) {
		return false;
	}

}
