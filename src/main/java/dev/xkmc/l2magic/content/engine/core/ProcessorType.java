package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2magic.content.engine.extension.CodecHolder;

public class ProcessorType<T extends Record & EntityProcessor<T>> extends CodecHolder<T> {

	public ProcessorType(MapCodec<T> codec) {
		super(codec);
	}

	public interface Factory<T extends Record & EntityProcessor<T>> {

		MapCodec<T> codec();

	}

}
