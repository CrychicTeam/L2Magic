package dev.xkmc.l2magic.content.engine.processor;

import dev.xkmc.l2magic.content.engine.core.EntityProcessor;

public interface SimpleServerProcessor<T extends Record & SimpleServerProcessor<T>> extends EntityProcessor<T> {

	@Override
	default boolean serverOnly() {
		return true;
	}
}
