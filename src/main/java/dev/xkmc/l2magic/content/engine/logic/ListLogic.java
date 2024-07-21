package dev.xkmc.l2magic.content.engine.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import java.util.List;

public record ListLogic(List<ConfiguredEngine<?>> children)
		implements ConfiguredEngine<ListLogic> {

	public static final MapCodec<ListLogic> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.list(ConfiguredEngine.CODEC).fieldOf("children").forGetter(e -> e.children)
	).apply(i, ListLogic::new));

	@Override
	public EngineType<ListLogic> type() {
		return EngineRegistry.LIST.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		for (var e : children) {
			ctx.execute(e);
		}
	}

}
