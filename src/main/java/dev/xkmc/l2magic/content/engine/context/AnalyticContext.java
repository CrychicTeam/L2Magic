package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.core.Verifiable;

import java.util.function.BiConsumer;

public record AnalyticContext(String path, BiConsumer<String, Verifiable> callback) {

	public AnalyticContext of(String s) {
		return new AnalyticContext(path + "/" + s, callback);
	}

	public void check(Verifiable obj) {
		callback.accept(path, obj);
	}

}
