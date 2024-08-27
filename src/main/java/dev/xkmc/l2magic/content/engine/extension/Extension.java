package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2serial.util.Wrappers;

public abstract class Extension<E, V> {

	private final Class<E> cls;

	public Extension(Class<E> cls) {
		this.cls = Wrappers.cast(cls);
	}

	public final Class<E> getType() {
		return cls;
	}

	public abstract E process(V val);

}