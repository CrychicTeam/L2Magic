package dev.xkmc.l2magic.content.engine.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public record KnockBlock(
		DoubleVariable speed,
		DoubleVariable damagePerBlock,
		DoubleVariable maxDamage
) implements IBlockProcessor<KnockBlock> {

	public static final MapCodec<KnockBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("speed", KnockBlock::speed),
			DoubleVariable.optionalCodec("damagePerBlock", KnockBlock::damagePerBlock),
			DoubleVariable.optionalCodec("maxDamage", KnockBlock::maxDamage)
	).apply(i, (a, b, c) -> new KnockBlock(a, b.orElse(DoubleVariable.ZERO), c.orElse(b.orElse(DoubleVariable.ZERO)))));

	@Override
	public EngineType<KnockBlock> type() {
		return EngineRegistry.KNOCK_BLOCK.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		var level = ctx.user().level();
		if (level.isClientSide()) return;
		var pos = BlockPos.containing(ctx.loc().pos());
		if (level.getBlockEntity(pos) != null) return;
		var state = level.getBlockState(pos);
		if (state.canBeReplaced()) return;
		level.setBlockAndUpdate(pos, state.getFluidState().createLegacyBlock());
		FallingBlockEntity e = fall(level, pos, state);
		e.setDeltaMovement(0, speed.eval(ctx), 0);
		e.setHurtsEntities((float) damagePerBlock().eval(ctx), (int) maxDamage().eval(ctx));
		level.addFreshEntity(e);
	}


	public static FallingBlockEntity fall(Level level, BlockPos pos, BlockState state) {
		var w = BlockStateProperties.WATERLOGGED;
		return new FallingBlockEntity(level,
				pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
				state.hasProperty(w) ? state.setValue(w, false) : state);
	}

}
