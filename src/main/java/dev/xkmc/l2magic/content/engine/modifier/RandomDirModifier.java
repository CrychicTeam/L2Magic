package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

public record RandomDirModifier() implements Modifier<RandomDirModifier> {

	public static MapCodec<RandomDirModifier> CODEC = MapCodec.unit(new RandomDirModifier());

	@Override
	public ModifierType<RandomDirModifier> type() {
		return EngineRegistry.RAND_DIR.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().setDir(new Vec3(
				ctx.rand().nextGaussian(), ctx.rand().nextGaussian(), ctx.rand().nextGaussian()
		).normalize());
	}

}
