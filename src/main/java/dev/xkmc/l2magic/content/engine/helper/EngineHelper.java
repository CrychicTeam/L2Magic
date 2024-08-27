package dev.xkmc.l2magic.content.engine.helper;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.AnalyticContext;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.core.ParameterizedVerifiable;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2serial.serialization.type_cache.RecordCache;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EngineHelper {

	private static final Map<Class<?>, EngineHelper> CACHE = new LinkedHashMap<>();

	public static EngineHelper get(Class<?> cls) {
		if (CACHE.containsKey(cls)) {
			return CACHE.get(cls);
		}
		var ans = of(cls);
		CACHE.put(cls, ans);
		return ans;
	}

	@Nullable
	public static Verifiable get(Verifiable obj, Class<?> cls, String[] path, int index) {
		if (index >= path.length) return obj;
		if (!cls.isRecord())
			throw new IllegalStateException("class " + cls.getSimpleName() + " is not a record");
		try {
			var cache = get(cls);
			String str = path[index];
			var e = cache.children.get(str);
			if (e != null) {
				Verifiable v = (Verifiable) e.get(obj);
				if (v != null) {
					return get(v, v.getClass(), path, index + 1);
				}
			}
			if (str.endsWith("]")) {
				String[] strs = str.substring(0, str.length() - 1).split("\\[");
				str = strs[0];
				String key = strs[1];
				e = cache.collections.get(str);
				if (e != null) {
					Object x = e.get(obj);
					if (x instanceof List<?> l) {
						int ind = Integer.parseInt(key);
						if (ind >= 0 && ind < l.size() && l.get(ind) instanceof Verifiable v)
							return get(v, v.getClass(), path, index + 1);
					} else if (x instanceof Map<?, ?> map) {
						if (map.get(key) instanceof Verifiable v)
							return get(v, v.getClass(), path, index + 1);
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalStateException("class " + cls.getSimpleName() + " failed analysis", e);
		}
		return null;
	}

	public static void analyze(Verifiable obj, AnalyticContext ctx, Class<?> cls) {
		if (!cls.isRecord())
			throw new IllegalStateException("class " + cls.getSimpleName() + " is not a record");
		ctx.check(obj);
		try {
			for (var e : get(cls).children.values()) {
				Verifiable v = (Verifiable) e.get(obj);
				if (v != null) {
					v.analyze(ctx.of(e.getName()));
				}
			}
			for (var e : get(cls).collections.values()) {
				Object x = e.get(obj);
				if (x instanceof List<?> l) {
					for (int i = 0; i < l.size(); i++) {
						if (l.get(i) instanceof Verifiable v)
							v.analyze(ctx.of(e.getName() + "[" + i + "]"));
					}
				} else if (x instanceof Map<?, ?> map) {
					for (var ent : map.entrySet()) {
						if (ent.getValue() instanceof Verifiable v)
							v.analyze(ctx.of(e.getName() + "[" + ent.getKey() + "]"));
					}
				}

			}
		} catch (Exception e) {
			throw new IllegalStateException("class " + cls.getSimpleName() + " failed analysis", e);
		}
	}

	public static void verifyFields(Verifiable obj, BuilderContext ctx, Class<?> cls) {
		if (!cls.isRecord())
			throw new IllegalStateException("class " + cls.getSimpleName() + " is not a record");
		try {
			var set = obj.verificationParameters();
			for (var e : get(cls).children.values()) {
				Verifiable v = (Verifiable) e.get(obj);
				if (v != null) {
					if (v instanceof ParameterizedVerifiable)
						v.verify(ctx.of(e.getName(), set));
					else
						v.verify(ctx.of(e.getName()));
				}
			}
			for (var e : get(cls).collections.values()) {
				Object x = e.get(obj);
				if (x instanceof List<?> l) {
					for (int i = 0; i < l.size(); i++) {
						if (l.get(i) instanceof Verifiable v)
							v.verify(ctx.of(e.getName() + "[" + i + "]"));
					}
				} else if (x instanceof Map<?, ?> map) {
					for (var ent : map.entrySet()) {
						if (ent.getValue() instanceof Verifiable v)
							v.verify(ctx.of(e.getName() + "[" + ent.getKey() + "]"));
					}
				}

			}
		} catch (Exception e) {
			throw new IllegalStateException("class " + cls.getSimpleName() + " failed configuration", e);
		}
	}

	private static EngineHelper of(Class<?> cls) {
		try {
			return new EngineHelper(cls);
		} catch (Exception e) {
			throw new IllegalStateException("class " + cls.getSimpleName() + " failed configuration", e);
		}
	}

	private final Map<String, Field> children = new LinkedHashMap<>();
	private final Map<String, Field> collections = new LinkedHashMap<>();

	public EngineHelper(Class<?> cls) throws Exception {
		assert cls.isRecord();
		var cache = RecordCache.get(cls);
		for (var e : cache.getFields()) {
			if (Verifiable.class.isAssignableFrom(e.getType())) {
				children.put(e.getName(), e);
			}
			if (List.class.isAssignableFrom(e.getType())) {
				collections.put(e.getName(), e);
			}
			if (Map.class.isAssignableFrom(e.getType())) {
				collections.put(e.getName(), e);
			}
		}
	}

	public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> cls, T[] vals) {
		return Codec.STRING.xmap(e -> {
			try {
				return Enum.valueOf(cls, e);
			} catch (Exception ex) {
				throw new IllegalArgumentException(e + " is not a valid " + cls.getSimpleName() + ". Valid values are: " + List.of(vals));
			}
		}, Enum::name);
	}

}
