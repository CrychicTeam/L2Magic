package dev.xkmc.l2magic.content.engine.extension;

import com.mojang.serialization.MapCodec;

public class CodecHolder<T extends IExtended<T>> extends ExtensionHolder<T> {

	private final MapCodec<T> codec;

	public CodecHolder(MapCodec<T> codec) {
		this.codec = codec;
	}

	public MapCodec<T> codec() {
		return codec;
	}

}
