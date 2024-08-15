package dev.xkmc.l2magic.init.data.spell.ice;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetPosModifier;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.*;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class IcyShatter extends SpellDataGenEntry {
    public static final ResourceKey<SpellAction> ICY_SHATTER = spell("icy_shatter");

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(SpellAction.lang(ICY_SHATTER.location()), "Icy Shatter");
    }

    @Override
    public void register(BootstrapContext<SpellAction> ctx) {
        new SpellAction(
                icyShatter(new DataGenContext(ctx)),
                Items.BLUE_ICE.asItem(), 3700,
                SpellCastType.INSTANT,
                SpellTriggerType.TARGET_POS
        ).verifyOnBuild(ctx, ICY_SHATTER);
    }

    private static ConfiguredEngine<?> icyShatter(DataGenContext ctx) {
        return new ListLogic(List.of(
                new MoveEngine(
                        List.of(
                                new SetPosModifier(
                                        DoubleVariable.of("PosX"),
                                        DoubleVariable.of("PosY+1"),
                                        DoubleVariable.of("PosZ")
                                )
                        ),
                        new ListLogic(List.of(
                                new BlockParticleInstance(  // Render
                                        Blocks.BLUE_ICE,
                                        DoubleVariable.ZERO,
                                        DoubleVariable.of("4"),
                                        IntVariable.of("300"),
                                        false
                                )
                        ))
                ),
                new DelayedIterator(
                        IntVariable.of("15"),
                        IntVariable.of("20"),
                        new ProcessorEngine(
                                SelectionType.ENEMY,
                                new ApproxCylinderSelector(
                                        DoubleVariable.of("8"),
                                        DoubleVariable.of("4")
                                ),
                                List.of(
                                        new EffectConditionProcessor(
                                                MobEffects.MOVEMENT_SLOWDOWN,//TODO LCEffects.ICE.get(),
                                                List.of(
                                                        new CastAtProcessor(
                                                                new ListLogic(List.of(
                                                                        new LoopIterator(  // Render
                                                                                IntVariable.of("20"),
                                                                                new SimpleParticleInstance(
                                                                                        ParticleTypes.SNOWFLAKE,
                                                                                        DoubleVariable.of("0.1")
                                                                                ).move(
                                                                                        new SetPosModifier(
                                                                                                DoubleVariable.of("PosX"),
                                                                                                DoubleVariable.of("PosY+0.5"),
                                                                                                DoubleVariable.of("PosZ")
                                                                                        ),
                                                                                        new RandomOffsetModifier(
                                                                                                RandomOffsetModifier.Type.RECT,
                                                                                                DoubleVariable.of("1"),
                                                                                                DoubleVariable.of("1"),
                                                                                                DoubleVariable.of("1")
                                                                                        ),
                                                                                        new SetDirectionModifier(
                                                                                                DoubleVariable.ZERO,
                                                                                                DoubleVariable.of("-1"),
                                                                                                DoubleVariable.ZERO
                                                                                        )
                                                                                ),
                                                                                null
                                                                        )
                                                                ))
                                                        ),
                                                        new DamageProcessor(ctx.damage(DamageTypes.FREEZE),
                                                                DoubleVariable.of("4"), true, true)
                                                ),
                                                List.of(new EffectProcessor(
                                                        MobEffects.MOVEMENT_SLOWDOWN,//TODO LCEffects.ICE.get(),
                                                        IntVariable.of("15"),
                                                        IntVariable.of("0"),
                                                        false, false
                                                ))
                                        )
                                )
                        ),
                        null
                )
        ));
    }
}
