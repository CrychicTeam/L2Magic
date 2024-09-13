package dev.xkmc.l2magic.content.entity.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;

public record BoundingData(
		DoubleVariable size,
		boolean fullBlockCollision
) implements Verifiable {

	public static final Codec<BoundingData> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("size", e -> e.size),
			Codec.BOOL.fieldOf("fullBlockCollision").forGetter(e -> e.fullBlockCollision)
	).apply(i, BoundingData::new));

}
