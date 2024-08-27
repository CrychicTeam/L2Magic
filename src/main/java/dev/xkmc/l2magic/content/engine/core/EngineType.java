package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2magic.content.engine.extension.CodecHolder;

public class EngineType<T extends Record & ConfiguredEngine<T>> extends CodecHolder<T> {

	public EngineType(MapCodec<T> codec) {
		super(codec);
	}

	public interface Factory<T extends Record & ConfiguredEngine<T>> {

		MapCodec<T> codec();

	}

}
