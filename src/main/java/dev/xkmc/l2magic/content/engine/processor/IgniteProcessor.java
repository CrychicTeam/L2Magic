package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public record IgniteProcessor(
        IntVariable burnTicks
) implements EntityProcessor<IgniteProcessor> {
    public static final MapCodec<IgniteProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IntVariable.codec("burnTicks", e -> e.burnTicks)
    ).apply(i, IgniteProcessor::new));

    @Override
    public ProcessorType<IgniteProcessor> type() {
        return EngineRegistry.IGNITE.get();
    }

    @Override
    public void process(Collection<LivingEntity> le, EngineContext ctx) {
        for (var e : le) {
            e.igniteForTicks(burnTicks.eval(ctx));
        }
    }
}
