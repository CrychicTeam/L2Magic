package dev.xkmc.l2magic.content.engine.extension;

public interface IExtended<T extends IExtended<T>> {

	ExtensionHolder<T> type();

}
