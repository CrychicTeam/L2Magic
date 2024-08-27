package dev.xkmc.l2magic.content.engine.core;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2core.util.DataGenOnly;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.extension.IExtended;
import dev.xkmc.l2magic.content.engine.iterator.BlockInRangeIterator;
import dev.xkmc.l2magic.content.engine.logic.DelayLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.VariableLogic;
import dev.xkmc.l2magic.content.engine.predicate.AndPredicate;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface ConfiguredEngine<T extends Record & ConfiguredEngine<T>>
		extends ParameterizedVerifiable, IExtended<T> {

	Codec<ConfiguredEngine<?>> CODEC = EngineRegistry.ENGINE.codec()
			.dispatch(ConfiguredEngine::type, EngineType::codec);

	static <T> RecordCodecBuilder<T, ConfiguredEngine<?>> codec(String str, Function<T, ConfiguredEngine<?>> func) {
		return CODEC.fieldOf(str).forGetter(func);
	}

	static <T> RecordCodecBuilder<T, Optional<ConfiguredEngine<?>>> optionalCodec(String str, Function<T, ConfiguredEngine<?>> func) {
		return CODEC.optionalFieldOf(str).forGetter(e -> Optional.ofNullable(func.apply(e)));
	}

	void execute(EngineContext ctx);

	EngineType<T> type();

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> move(Modifier<?>... mod) {
		return new MoveEngine(List.of(mod), this);
	}

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> delay(IntVariable delay) {
		return new DelayLogic(delay, this);
	}

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> withVariables(Map<String, DoubleVariable> vars) {
		ConfiguredEngine<?> self = this;
		for (var ent : vars.entrySet()) {
			self = new VariableLogic(ent.getKey(), ent.getValue(), self);
		}
		return self;
	}

	default ConfiguredEngine<?> circular(
			DoubleVariable radius,
			DoubleVariable delayPerBlock,
			boolean plane,
			@Nullable String variable,
			IPredicate... predicates
	) {
		return new BlockInRangeIterator(radius, delayPerBlock, plane,
				new PredicateLogic(new AndPredicate(List.of(predicates)),
						this, null),
				variable);
	}

}