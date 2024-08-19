package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record EffectConditionProcessor(
        Holder<MobEffect> eff,
        List<EntityProcessor<?>> action,
        List<EntityProcessor<?>> fallback
) implements EntityProcessor<EffectConditionProcessor> {

    public static final MapCodec<EffectConditionProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(e -> e.eff),
            Codec.list(EntityProcessor.CODEC).fieldOf("action").forGetter(e -> e.action),
            Codec.list(EntityProcessor.CODEC).fieldOf("fallback").forGetter(e -> e.fallback)
    ).apply(i, EffectConditionProcessor::new));

    @Override
    public ProcessorType<EffectConditionProcessor> type() {
        return EngineRegistry.EFFECT_CONDITION.get();
    }

    @Override
    public void process(Collection<LivingEntity> le, EngineContext ctx) {
        Map<Boolean, List<LivingEntity>> partitioned = le.stream()
                .collect(Collectors.partitioningBy(i -> i.hasEffect(eff)));
        if (action().size() > 0)
            action().forEach(p->p.process(partitioned.get(true), ctx));
        if (fallback().size() > 0)
            fallback().forEach(p->p.process(partitioned.get(false), ctx));
    }
}
