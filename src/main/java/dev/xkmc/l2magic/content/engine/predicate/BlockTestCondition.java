package dev.xkmc.l2magic.content.engine.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ContextPredicate;
import dev.xkmc.l2magic.content.engine.core.PredicateType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public record BlockTestCondition(
		Type test
) implements ContextPredicate<BlockTestCondition> {

	public enum Type {
		REPLACEABLE((e, l, p) -> e.canBeReplaced()),
		REQUIRES_TOOL((e, l, p) -> e.requiresCorrectToolForDrops()),
		BLOCKS_MOTION((e, l, p) -> e.blocksMotion()),
		PUSHABLE(BlockTestCondition::pushable);

		private final StateTest pred;

		Type(StateTest pred) {
			this.pred = pred;
		}

		public BlockTestCondition get() {
			return new BlockTestCondition(this);
		}

	}

	private static boolean pushable(BlockState e, Level l, BlockPos p) {
		var hardness = e.getDestroySpeed(l, p);
		var push = e.getPistonPushReaction();
		return hardness >= 0 && hardness <= 48 && l.getBlockEntity(p) == null &&
				(push == PushReaction.NORMAL || push == PushReaction.PUSH_ONLY);
	}

	public static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static final MapCodec<BlockTestCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			TYPE_CODEC.fieldOf("test").forGetter(BlockTestCondition::test)
	).apply(i, BlockTestCondition::new));

	@Override
	public PredicateType<BlockTestCondition> type() {
		return EngineRegistry.BLOCK_TEST.get();
	}

	@Override
	public boolean test(EngineContext ctx) {
		var l = ctx.user().level();
		var p = BlockPos.containing(ctx.loc().pos());
		return test.pred.test(l.getBlockState(p), l, p);
	}

	public interface StateTest {

		boolean test(BlockState state, Level level, BlockPos pos);

	}

}
