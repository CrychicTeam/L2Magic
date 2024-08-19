package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public record CastAtProcessor(
        ConfiguredEngine<?> cast
) implements EntityProcessor<CastAtProcessor> {

    public static final MapCodec<CastAtProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ConfiguredEngine.codec("cast", e -> e.cast)
    ).apply(i, CastAtProcessor::new));

    @Override
    public ProcessorType<CastAtProcessor> type() {
        return EngineRegistry.CAST_AT.get();
    }

    @Override
    public void process(Collection<LivingEntity> le, EngineContext ctx) {
        for (LivingEntity e: le) {
            var ctxnew = ctx.with(LocationContext.of(e.position(), LocationContext.UP));
            cast.execute(ctxnew);
        }
    }
}
