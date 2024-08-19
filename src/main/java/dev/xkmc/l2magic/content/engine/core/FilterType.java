package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.MapCodec;

public interface FilterType<T extends Record & EntityFilter<T>> {

	MapCodec<T> codec();

}
