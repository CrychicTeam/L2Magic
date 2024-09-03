package dev.xkmc.l2magic.content.engine.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record ScheduleTick(
		IntVariable tick,
		Block block
) implements IBlockProcessor<ScheduleTick> {

	public static final MapCodec<ScheduleTick> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("tick", ScheduleTick::tick),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(ScheduleTick::block)
	).apply(i, ScheduleTick::new));

	@Override
	public EngineType<ScheduleTick> type() {
		return EngineRegistry.SCHEDULE_TICK.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		var pos = BlockPos.containing(ctx.loc().pos());
		var level = ctx.user().level();
		if (level.isClientSide()) return;
		level.scheduleTick(pos, block, tick.eval(ctx));
	}

}
